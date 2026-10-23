package trap.report;

import org.junit.jupiter.api.Test;
import trap.client.TrapDataClient;
import trap.config.TrapProperties;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

class DownloadServiceTest {

    @Test
    void downloadFiles_retriesThreeTimesBeforeSucceeding() throws Exception {
        String type = "retry-test-" + UUID.randomUUID();
        Path file = Path.of(type + ".csv");
        TrapDataClient trapDataClient = mock(TrapDataClient.class);
        var trapProperties = new TrapProperties(
                new TrapProperties.Download(List.of(type), Map.of(type, "file-id")),
                new TrapProperties.Report("report-", "yyyyMMdd", 8),
                new TrapProperties.Http(Duration.ofSeconds(5), Duration.ofSeconds(30))
        );
        when(trapDataClient.getFileContent("file-id"))
                .thenThrow(new IllegalStateException("temporary failure"))
                .thenThrow(new IllegalStateException("temporary failure"))
                .thenThrow(new IllegalStateException("temporary failure"))
                .thenReturn("downloaded");

        try {
            new DownloadService(trapDataClient, trapProperties).downloadFiles();

            verify(trapDataClient, times(4)).getFileContent("file-id");
            assertEquals("downloaded", Files.readString(file));
        } finally {
            Files.deleteIfExists(file);
        }
    }
}
