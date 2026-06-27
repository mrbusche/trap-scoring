package trap.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import trap.client.TrapDataClient;
import trap.config.TrapProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RequiredArgsConstructor
@Service
@Slf4j
public class DownloadService {
    private final TrapDataClient trapDataClient;
    private final TrapProperties trapProperties;

    public void downloadFiles() {
        var start = Instant.now();
        log.info("Started downloading files");

        var failures = new ArrayList<Throwable>();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var futures = trapProperties.download().trapTypes().stream()
                    .map(type -> executor.submit(() -> {
                        try {
                            processFile(type);
                        } catch (IOException e) {
                            throw new IllegalStateException("Failed to process " + type, e);
                        }
                        return null;
                    }))
                    .toList();

            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (ExecutionException e) {
                    failures.add(e.getCause());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Download process was interrupted", e);
            throw new IllegalStateException("Download interrupted", e);
        }

        if (!failures.isEmpty()) {
            var failure = new IllegalStateException("One or more downloads failed");
            failures.forEach(failure::addSuppressed);
            throw failure;
        }

        var duration = Duration.between(start, Instant.now());
        log.info("Files downloaded in {} ms", duration.toMillis());
    }

    private void processFile(String type) throws IOException {
        var fileId = Objects.requireNonNull(trapProperties.download().fileIds().get(type), "Missing file ID for " + type);

        log.info("Downloading {} file", type);
        String content = trapDataClient.getFileContent(fileId);
        if (content == null) {
            throw new IllegalStateException("Failed to download " + type + ": received null content");
        }

        String cleanContent = content.replaceAll(" {2}", " ");
        var path = Path.of(type + ".csv");
        Files.writeString(path, cleanContent, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        log.info("Finished downloading {} file", type);
    }
}
