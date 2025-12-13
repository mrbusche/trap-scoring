package trap;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import trap.report.ReportService;

@SpringBootTest
class ApplicationTest {

    @MockitoBean
    private ReportService reportService;

    @Test
    void contextLoads() {
        // the Spring Context loaded successfully.
    }
}
