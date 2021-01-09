package trap.report;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;
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
        long fileSize = 0;

        Set<String> files = Stream.of(new File(".").listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
        for (String file : files) {
            if (file.endsWith(".xlsx")) {
                System.out.println("xlsx file");
                System.out.println(file);
                fileExists = true;
                fileSize = new File(file).length();
                break;
            }
        }
        assertThat(fileExists).isTrue();
        assertThat(fileSize).isGreaterThan(templateSize);
    }
}
