package trap.report;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReportHelperTest {

    @Test
    void testFileCreated() {
        boolean fileExists = false;
        String filename = "";
        Set<String> files = Stream.of(Objects.requireNonNull(new File(".").listFiles()))
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
        for (String file : files) {
            if (file.endsWith(".xlsx")) {
                fileExists = true;
                filename = file;
                break;
            }
        }
        long fileSize = new File(filename).length();
        assertThat(fileSize).isGreaterThan(125813L);
        assertThat(fileExists).isTrue();

    }
}
