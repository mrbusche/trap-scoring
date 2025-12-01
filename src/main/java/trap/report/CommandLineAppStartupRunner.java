package trap.report;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private final ReportService reportService;

    @Override
    public void run(String @NonNull ... args) throws Exception {
        reportService.generateExcelFile();
    }
}
