package trap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.web.service.registry.ImportHttpServices;
import trap.client.TrapDataClient;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableResilientMethods
@ImportHttpServices(group = "metabase", types = TrapDataClient.class)
class Application {
    private Application() {
    }

    static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
