package trap

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import java.util.logging.Logger

@SpringBootApplication
class Application {
    @Bean
    fun commandLineRunner(ctx: ApplicationContext?): CommandLineRunner {
        return CommandLineRunner { LOG.info("File created") }
    }

    companion object {
        private val LOG = Logger.getLogger(Application::class.java.name)
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args).close()
        }
    }
}