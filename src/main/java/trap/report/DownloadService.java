package trap.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import trap.client.TrapDataClient;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

@Service
public class DownloadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadService.class);

    private final TrapDataClient trapDataClient;

    private static final Map<String, String> FILE_IDS = Map.of(
            "singles", "8648faf9-42e8-4a9c-b55d-2f251349de7f",
            "doubles", "5d5a78a5-2356-477f-b1b8-fe6ee11d25b1",
            "handicap", "69ca55d9-3e18-45bc-b57f-73aeb205ece8",
            "skeet", "c697d744-0e06-4c3f-a640-fea02f9c9ecd",
            "clays", "2c6edb1a-a7ee-43c2-8180-ad199a57be55",
            "fivestand", "3c5aecf2-a9f2-49b2-a11f-36965cb1a964",
            "doublesskeet", "bdd61066-6e29-4242-b6e9-adf286c2c4ae"
    );

    public DownloadService(TrapDataClient trapDataClient) {
        this.trapDataClient = trapDataClient;
    }

    public void downloadFiles(String[] trapTypes) {
        var start = Instant.now();
        LOGGER.info("Started downloading files");

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            var tasks = Arrays.stream(trapTypes)
                    .map(type -> (Callable<Void>) () -> {
                        processFile(type, FILE_IDS.get(type));
                        return null;
                    }).toList();

            executor.invokeAll(tasks);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            LOGGER.error("Download process was interrupted", e);
            throw new RuntimeException("Download interrupted", e);
        }

        var duration = Duration.between(start, Instant.now());
        LOGGER.info("Files downloaded in {} ms", duration.toMillis());
    }

    private void processFile(String type, String fileId) {
        try {
            LOGGER.info("Downloading {} file", type);
            String content = trapDataClient.getFileContent(fileId);

            if (content == null) {
                LOGGER.error("Failed to download {}: Received null content", type);
                return;
            }

            LOGGER.info("Finished downloading {} file", type);

            String cleanContent = content.replaceAll(" {2}", " ");
            var path = Path.of(type + ".csv");
            Files.writeString(path, cleanContent, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            LOGGER.info("Finished replacing double spaces for {} file", type);
        } catch (Exception e) {
            LOGGER.error("Error processing {} file", type, e);
        }
    }
}