package trap.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface TrapDataClient {

    /**
     * Fetches the CSV content for a specific file ID.
     *
     * @param fileId The UUID of the file to download (e.g., "8648faf9-42e8...").
     * @return The raw CSV string content.
     */
    @GetExchange("/{fileId}.csv")
    String getFileContent(@PathVariable("fileId") String fileId);
}
