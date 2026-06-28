package trap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
class Application {
    private Application() {
    }

    static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
