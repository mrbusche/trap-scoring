package trap.config;

import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

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
}
