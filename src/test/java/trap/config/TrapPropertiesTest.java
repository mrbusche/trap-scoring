package trap.config;

import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TrapPropertiesTest {

    @Test
    void acceptsCompleteConfiguration() {
        var properties = new TrapProperties(
                new TrapProperties.Download(List.of("singles", "doubles"), Map.of("singles", "id-1", "doubles", "id-2")),
                new TrapProperties.Report("report-", "yyyyMMdd", 8)
        );

        try (var factory = Validation.buildDefaultValidatorFactory()) {
            assertTrue(factory.getValidator().validate(properties).isEmpty());
        }
    }

    @Test
    void rejectsUnsupportedTypesDuplicatesAndMissingFileIds() {
        var properties = new TrapProperties(
                new TrapProperties.Download(List.of("unknown", "singles", "singles"), Map.of("unknown", "id-1")),
                new TrapProperties.Report("report-", "yyyyMMdd", 8)
        );

        try (var factory = Validation.buildDefaultValidatorFactory()) {
            var messages = factory.getValidator().validate(properties).stream()
                    .map(violation -> violation.getMessage())
                    .collect(Collectors.toSet());

            assertTrue(messages.containsAll(Set.of(
                    "trap.download.trap-types contains an unsupported type",
                    "trap.download.trap-types must not contain duplicates",
                    "trap.download.file-ids must define an ID for every configured trap type"
            )));
        }
    }
}
