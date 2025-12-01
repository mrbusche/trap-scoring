package trap.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Service
public class DownloadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadService.class);

    private final RestClient restClient;

    private static final Map<String, String> FILE_URLS = Map.of(
            "singles", "/8648faf9-42e8-4a9c-b55d-2f251349de7f.csv",
            "doubles", "/5d5a78a5-2356-477f-b1b8-fe6ee11d25b1.csv",
            "handicap", "/69ca55d9-3e18-45bc-b57f-73aeb205ece8.csv",
            "skeet", "/c697d744-0e06-4c3f-a640-fea02f9c9ecd.csv",
            "clays", "/2c6edb1a-a7ee-43c2-8180-ad199a57be55.csv",
            "fivestand", "/3c5aecf2-a9f2-49b2-a11f-36965cb1a964.csv",
            "doublesskeet", "/bdd61066-6e29-4242-b6e9-adf286c2c4ae.csv"
    );

    // Spring Boot automatically provides a pre-configured Builder prototype
    public DownloadService(RestClient.Builder builder) {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(30_000);
        factory.setReadTimeout(120_000);

        this.restClient = builder
                .baseUrl("https://metabase.sssfonline.com/public/question")
                .requestFactory(factory)
                .build();
    }

    public void downloadFiles(String[] trapTypes) {
        var start = Instant.now();
        LOGGER.info("Started downloading files");

        // Stable Java 21+ Virtual Thread Executor
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = new CompletableFuture<?>[trapTypes.length];

            for (int i = 0; i < trapTypes.length; i++) {
                String type = trapTypes[i];
                // Submit task and store future to ensure we wait for completion
                futures[i] = CompletableFuture.runAsync(() ->
                        processFile(type, FILE_URLS.get(type)), executor);
            }

            // Wait for all downloads to finish
            CompletableFuture.allOf(futures).join();
        }

        var duration = Duration.between(start, Instant.now());
        LOGGER.info("Files downloaded in {} ms", duration.toMillis());
    }

    private void processFile(String type, String urlSuffix) {
        try {
            LOGGER.info("Downloading {} file", type);

            String content = restClient.get()
                    .uri(urlSuffix)
                    .retrieve()
                    .body(String.class);

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
