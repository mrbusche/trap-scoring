package trap.report;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReportHelperTest {

    @Test
    void testFileCreated() throws IOException {
        boolean fileExists = false;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("template.xlsx");
        long templateSize = IOUtils.toByteArray(is).length;

        String excelFileName = Stream.of(new File(".").listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .filter(fileName -> fileName.endsWith(".xlsx"))
                .findFirst()
                .orElseThrow();
        long fileSize = new File(excelFileName).length();
        assertThat(fileSize).isGreaterThan(templateSize);

        assertThat(fileSize).isGreaterThan(templateSize);
    }
}
