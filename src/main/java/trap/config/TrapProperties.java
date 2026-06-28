package trap.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Validated
@ConfigurationProperties(prefix = "trap")
public record TrapProperties(@Valid @NotNull Download download, @Valid @NotNull Report report, @Valid @NotNull Http http) {
    public record Download(@NotEmpty List<@NotBlank String> trapTypes, @NotEmpty Map<@NotBlank String, @NotBlank String> fileIds) {
    }

    public record Report(@NotBlank String outputFilePrefix, @NotBlank String outputFilePattern, @Min(1) @Max(12) int seasonCutoffMonth) {
        public String generateFilename() {
            return outputFilePrefix + LocalDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(outputFilePattern)) + ".xlsx";
        }
    }

    public record Http(@NotNull Duration connectTimeout, @NotNull Duration readTimeout) {
    }
}
