package trap.report;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import trap.model.RoundScore;

import java.io.File;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private DownloadService downloadService;

    @Spy
    private TrapService trapService = new TrapService();

    @Mock
    private TrapDataRepository trapDataRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    void testGenerateExcelFile() throws Exception {
        RoundScore mockScore = new RoundScore(1, "Event", 100, "Location", "Date", "Squad", "Team A", "Athlete A", "Varsity", "M", 25, 25, 0, 0, 0, 0, 0, 0, "singles");

        when(trapDataRepository.readRoundScores(anyString())).thenReturn(List.of(mockScore));

        reportService.generateExcelFile();

        verify(downloadService, times(1)).downloadFiles(any());
        verify(trapService, atLeastOnce()).calculatePlayerRoundTotals(any());

        String dateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        File generatedFile = new File("league-data-" + dateString + ".xlsx");

        if (generatedFile.exists()) {
            generatedFile.delete();
        }
    }

    @Test
    void testCsvParsingLogic() {
        // Test the parsing logic specifically using the repository refactor
        TrapDataRepository repo = new TrapDataRepository();
        String csvData = "Header1,Header2,Header3,Header4,Header5,Header6,Header7,Header8,Header9,Header10,Header11,Header12,Header13,Header14,Header15,Header16,Header17,Header18,Header19,Header20\n" +
                "0,123,Singles,10,Ankeny,2024,Squad,Team A,Player 1,,Varsity,M,25,25,0,0,0,0,0,0";

        StringReader stringReader = new StringReader(csvData);
        List<RoundScore> scores = repo.parseCsv(stringReader, "singles");

        assertEquals(1, scores.size());
        assertEquals("Player 1", scores.getFirst().athlete());
        assertEquals(25, scores.getFirst().round1());
    }
}
