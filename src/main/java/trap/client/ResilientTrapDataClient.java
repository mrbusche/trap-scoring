package trap.client;

import lombok.RequiredArgsConstructor;
import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@Service
@RequiredArgsConstructor
public class ResilientTrapDataClient {
    private final TrapDataClient trapDataClient;

    @ConcurrencyLimit(4)
    @Retryable(
            includes = {ResourceAccessException.class, HttpServerErrorException.class, HttpClientErrorException.TooManyRequests.class},
            maxRetries = 3,
            delay = 200,
            jitter = 50,
            multiplier = 2,
            maxDelay = 1000)
    public String getFileContent(String fileId) {
        return trapDataClient.getFileContent(fileId);
    }
}
