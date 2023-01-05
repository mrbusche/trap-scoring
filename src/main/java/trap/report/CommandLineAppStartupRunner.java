package trap.report;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private final ReportHelper reportHelper;

    @Override
    public void run(String... args) throws Exception {
        reportHelper.doItAll();
    }
}
