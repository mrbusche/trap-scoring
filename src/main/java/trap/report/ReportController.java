package trap.report;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportHelper reportHelper;

    @RequestMapping("/export")
    public String export() throws IOException {
        reportHelper.doItAll();
        return "Report complete";
    }
}
