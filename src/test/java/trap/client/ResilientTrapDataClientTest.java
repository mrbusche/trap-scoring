package trap.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import trap.config.TrapProperties;
import trap.report.DownloadService;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class ResilientTrapDataClientTest {
    @Autowired
    private ResilientTrapDataClient client;

    @MockitoBean
    private TrapDataClient trapDataClient;

    @Test
    void retriesTransientFailuresAndRateLimits() {
        var rateLimit = HttpClientErrorException.create(HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests",
                HttpHeaders.EMPTY, new byte[0], StandardCharsets.UTF_8);
        var serverError = HttpServerErrorException.create(HttpStatus.SERVICE_UNAVAILABLE, "Unavailable",
                HttpHeaders.EMPTY, new byte[0], StandardCharsets.UTF_8);
        when(trapDataClient.getFileContent("file-id"))
                .thenThrow(new ResourceAccessException("connection failed"))
                .thenThrow(rateLimit)
                .thenThrow(serverError)
                .thenReturn("downloaded");

        assertEquals("downloaded", client.getFileContent("file-id"));
        verify(trapDataClient, times(4)).getFileContent("file-id");
    }

    @Test
    void propagatesLastFailureAfterRetries() {
        var failure = new ResourceAccessException("connection failed");
        when(trapDataClient.getFileContent("file-id")).thenThrow(failure);

        assertSame(failure, assertThrows(ResourceAccessException.class, () -> client.getFileContent("file-id")));
        verify(trapDataClient, times(4)).getFileContent("file-id");
    }

    @Test
    void doesNotRetryPermanentClientErrors() {
        var failure = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "Not Found",
                HttpHeaders.EMPTY, new byte[0], StandardCharsets.UTF_8);
        when(trapDataClient.getFileContent("file-id")).thenThrow(failure);

        assertSame(failure, assertThrows(HttpClientErrorException.class, () -> client.getFileContent("file-id")));
        verify(trapDataClient).getFileContent("file-id");
    }

    @Test
    void downloadServiceUsesTheResilientClient(@TempDir Path tempDir) throws Exception {
        String type = tempDir.resolve("singles").toString();
        var properties = new TrapProperties(
                new TrapProperties.Download(List.of(type), Map.of(type, "file-id")),
                new TrapProperties.Report("report-", "yyyyMMdd", 8));
        when(trapDataClient.getFileContent("file-id"))
                .thenThrow(new ResourceAccessException("connection failed"))
                .thenReturn("downloaded");

        new DownloadService(client, properties).downloadFiles();

        assertEquals("downloaded", Files.readString(Path.of(type + ".csv")));
        verify(trapDataClient, times(2)).getFileContent("file-id");
    }

    @Test
    void limitsConcurrentVirtualThreadRequests() throws Exception {
        var active = new AtomicInteger();
        var peak = new AtomicInteger();
        var entered = new CountDownLatch(4);
        var fifthEntered = new CountDownLatch(5);
        var release = new CountDownLatch(1);
        when(trapDataClient.getFileContent(anyString())).thenAnswer(invocation -> {
            int current = active.incrementAndGet();
            peak.accumulateAndGet(current, Math::max);
            entered.countDown();
            fifthEntered.countDown();
            try {
                if (!release.await(10, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("Timed out waiting to release requests");
                }
                return "downloaded";
            } finally {
                active.decrementAndGet();
            }
        });

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = IntStream.range(0, 50)
                    .mapToObj(i -> executor.submit(() -> client.getFileContent("file-" + i)))
                    .toList();
            try {
                assertTrue(entered.await(10, TimeUnit.SECONDS));
                assertFalse(fifthEntered.await(500, TimeUnit.MILLISECONDS));
            } finally {
                release.countDown();
            }
            for (var future : futures) {
                assertEquals("downloaded", future.get(10, TimeUnit.SECONDS));
            }
        }

        assertEquals(4, peak.get());
        verify(trapDataClient, times(50)).getFileContent(anyString());
    }
}
