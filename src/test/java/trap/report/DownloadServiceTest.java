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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
