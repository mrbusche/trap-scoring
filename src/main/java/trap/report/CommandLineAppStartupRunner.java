package trap.report;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private final ReportHelper reportHelper;

    @Override
    public void run(String... args) throws Exception {
        reportHelper.generateExcelFile();
    }
}
