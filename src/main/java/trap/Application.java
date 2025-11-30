package trap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
class Application {
    void main(String[] args) {
        SpringApplication.run(Application.class, args).close();
    }
}
