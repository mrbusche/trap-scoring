package trap.report;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReportHelperTest {

    @Test
    void testFileCreated() {
        String excelFileName = Stream.of(Objects.requireNonNull(new File(".").listFiles())).filter(file -> !file.isDirectory()).map(File::getName).filter(fileName -> fileName.endsWith(".xlsx")).findFirst().orElseThrow();
        long fileSize = new File(excelFileName).length();
        assertThat(fileSize).isGreaterThan(125813L);
    }
}