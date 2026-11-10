package trap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import trap.client.TrapDataClient;
import trap.report.ReportService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

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

    @Test
    void mainStartsSpringApplication() {
        String[] args = {"--spring.profiles.active=test"};

        try (var springApplication = mockStatic(org.springframework.boot.SpringApplication.class)) {
            Application.main(args);

            springApplication.verify(() ->
                    org.springframework.boot.SpringApplication.run(Application.class, args));
        }
    }
}
