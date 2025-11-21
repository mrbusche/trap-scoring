package trap.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.Executors;

public class DownloadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadService.class);

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final Map<String, String> FILE_URLS = Map.of(
            "singles", "https://metabase.sssfonline.com/public/question/8648faf9-42e8-4a9c-b55d-2f251349de7f.csv",
            "doubles", "https://metabase.sssfonline.com/public/question/5d5a78a5-2356-477f-b1b8-fe6ee11d25b1.csv",
            "handicap", "https://metabase.sssfonline.com/public/question/69ca55d9-3e18-45bc-b57f-73aeb205ece8.csv",
            "skeet", "https://metabase.sssfonline.com/public/question/c697d744-0e06-4c3f-a640-fea02f9c9ecd.csv",
            "clays", "https://metabase.sssfonline.com/public/question/2c6edb1a-a7ee-43c2-8180-ad199a57be55.csv",
            "fivestand", "https://metabase.sssfonline.com/public/question/3c5aecf2-a9f2-49b2-a11f-36965cb1a964.csv",
            "doublesskeet", "https://metabase.sssfonline.com/public/question/bdd61066-6e29-4242-b6e9-adf286c2c4ae.csv"
    );

    public void downloadFiles(String[] trapTypes) {
        var start = Instant.now();
        LOGGER.info("Started downloading files");

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (var type : trapTypes) {
                executor.submit(() -> processFile(type, FILE_URLS.get(type)));
            }
        }

        var duration = Duration.between(start, Instant.now());
        LOGGER.info("Files downloaded in {} ms", duration.toMillis());
    }

    private void processFile(String type, String url) {
        try {
            LOGGER.info("Downloading {} file", type);

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

            // Using BodyHandlers.ofString to manipulate content in memory before saving
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() != 200) {
                LOGGER.error("Failed to download {}: HTTP {}", type, response.statusCode());
                return;
            }

            LOGGER.info("Finished downloading {} file", type);

            // Replace in memory string instead of reading file back from disk
            String cleanContent = response.body().replaceAll(" {2}", " ");

            var path = Path.of(type + ".csv");
            Files.writeString(path, cleanContent, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            LOGGER.info("Finished replacing double spaces for {} file", type);

        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            LOGGER.error("Error processing {} file", type, e);
        }
    }
}
