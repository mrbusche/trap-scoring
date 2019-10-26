package trap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import trap.report.ReportHelper;

import java.io.IOException;
import java.util.logging.Logger;

@SpringBootApplication
@RequiredArgsConstructor
public class Application {

    private static final Logger LOG = Logger.getLogger(Application.class.getName());
    private final ReportHelper reportHelper;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) throws IOException {
        StringBuilder result = new StringBuilder();
        result.append(reportHelper.doItAll());
        return args -> LOG.info("Welcome to Trap App");
    }

}