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
import java.util.Map;
import java.util.concurrent.Executors;

public class DownloadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadService.class);

    private static final Map<String, String> FILE_URLS = Map.of("singles", "https://metabase.sssfonline.com/public/question/8648faf9-42e8-4a9c-b55d-2f251349de7f.csv", "doubles", "https://metabase.sssfonline.com/public/question/5d5a78a5-2356-477f-b1b8-fe6ee11d25b1.csv", "handicap", "https://metabase.sssfonline.com/public/question/69ca55d9-3e18-45bc-b57f-73aeb205ece8.csv", "skeet", "https://metabase.sssfonline.com/public/question/c697d744-0e06-4c3f-a640-fea02f9c9ecd.csv", "clays", "https://metabase.sssfonline.com/public/question/2c6edb1a-a7ee-43c2-8180-ad199a57be55.csv", "fivestand", "https://metabase.sssfonline.com/public/question/3c5aecf2-a9f2-49b2-a11f-36965cb1a964.csv", "doublesskeet", "https://metabase.sssfonline.com/public/question/bdd61066-6e29-4242-b6e9-adf286c2c4ae.csv");

    public void downloadFiles(String[] trapTypes) {
        var start = System.currentTimeMillis();
        LOGGER.info("Started downloading files");

        var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(120)).followRedirects(HttpClient.Redirect.NORMAL).build();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            for (var type : trapTypes) {
                executor.submit(() -> {
                    try {
                        LOGGER.info("Downloading {} file", type);

                        var request = HttpRequest.newBuilder().uri(URI.create(FILE_URLS.get(type))).timeout(Duration.ofSeconds(120)) // Set read timeout
                                .GET().build();

                        var path = Path.of(type + ".csv");

                        // Download file directly to disk using standard Java NIO
                        client.send(request, HttpResponse.BodyHandlers.ofFile(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE));

                        LOGGER.info("Finished downloading {} file", type);

                        LOGGER.info("Replacing double spaces for {} file", type);
                        var charset = StandardCharsets.UTF_8;
                        var content = Files.readString(path, charset).replaceAll(" {2}", " ");
                        Files.writeString(path, content, charset);
                        LOGGER.info("Finished replacing double spaces for {} file", type);

                    } catch (IOException | InterruptedException e) {
                        LOGGER.error("Error processing {} file", type, e);
                        // Restore interrupted status if needed, though this task is ending.
                        if (e instanceof InterruptedException) {
                            Thread.currentThread().interrupt();
                        }
                    }
                });
            }
        }

        LOGGER.info("Files downloaded in {} ms", System.currentTimeMillis() - start);
    }
}