package trap.report;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private final ReportService reportService;

    public CommandLineAppStartupRunner(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    public void run(String... args) throws Exception {
        reportService.generateExcelFile();
    }
}