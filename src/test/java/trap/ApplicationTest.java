package trap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import trap.client.TrapDataClient;
import trap.report.ReportService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ApplicationTest {

    @Autowired
    private TrapDataClient trapDataClient;

    @MockitoBean
    private ReportService reportService;

    @Test
    void contextLoads() {
        assertNotNull(trapDataClient);
    }
}
