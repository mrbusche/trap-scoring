package trap.report;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import trap.client.ResilientTrapDataClient;
import trap.client.TrapDataClient;
import trap.config.TrapProperties;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

class DownloadServiceTest {

    @Test
    void downloadFiles_normalizesCurlyApostrophes(@TempDir Path tempDir) throws Exception {
        String type = tempDir.resolve("singles").toString();
        TrapDataClient trapDataClient = mock(TrapDataClient.class);
        var trapProperties = new TrapProperties(
                new TrapProperties.Download(List.of(type), Map.of(type, "file-id")),
                new TrapProperties.Report("report-", "yyyyMMdd", 8)
        );
        when(trapDataClient.getFileContent("file-id")).thenReturn("Sportsmen’s  Club's");

        new DownloadService(new ResilientTrapDataClient(trapDataClient), trapProperties).downloadFiles();

        assertEquals("Sportsmen's Club's", Files.readString(Path.of(type + ".csv")));
    }

    @Test
    void downloadFilesAggregatesFailuresAndPreservesCause() {
        String type = "singles";
        TrapDataClient trapDataClient = mock(TrapDataClient.class);
        var trapProperties = new TrapProperties(
                new TrapProperties.Download(List.of(type), Map.of(type, "file-id")),
                new TrapProperties.Report("report-", "yyyyMMdd", 8)
        );
        when(trapDataClient.getFileContent("file-id")).thenReturn(null);

        IllegalStateException failure = assertThrows(IllegalStateException.class,
                () -> new DownloadService(new ResilientTrapDataClient(trapDataClient), trapProperties).downloadFiles());

        assertEquals("One or more downloads failed", failure.getMessage());
        assertEquals(1, failure.getSuppressed().length);
        assertEquals("Failed to download singles: received null content", failure.getSuppressed()[0].getMessage());
    }

    @Test
    void downloadFilesAggregatesMissingFileId() {
        var trapProperties = new TrapProperties(
                new TrapProperties.Download(List.of("singles"), Map.of()),
                new TrapProperties.Report("report-", "yyyyMMdd", 8)
        );

        IllegalStateException failure = assertThrows(IllegalStateException.class,
                () -> new DownloadService(mock(ResilientTrapDataClient.class), trapProperties).downloadFiles());

        assertEquals("One or more downloads failed", failure.getMessage());
        assertInstanceOf(NullPointerException.class, failure.getSuppressed()[0]);
    }
}
