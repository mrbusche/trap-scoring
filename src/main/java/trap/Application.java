package trap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    static void main(String[] args) {
        System.setProperty("spring.threads.virtual.enabled", "true");
        SpringApplication.run(Application.class, args).close();
    }
}
