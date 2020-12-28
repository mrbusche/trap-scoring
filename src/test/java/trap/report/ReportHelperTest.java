package trap.report;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReportHelperTest {

    @Test
    public void testFileCreated() {
        boolean fileExists = false;
        Set<String> files = Stream.of(new File(".").listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
        for (String file : files) {
            if (file.endsWith(".xlsx")) {
                fileExists = true;
                break;
            }
        }
        assertThat(fileExists).isTrue();
    }
}
