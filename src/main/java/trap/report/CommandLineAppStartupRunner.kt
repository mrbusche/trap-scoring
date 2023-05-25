package trap.report

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("!test")
@Component
class CommandLineAppStartupRunner : CommandLineRunner {
    private val reportHelper: ReportHelper = ReportHelper()

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        reportHelper.doItAll()
    }
}
