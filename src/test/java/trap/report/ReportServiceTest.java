package trap.report;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import trap.model.IndividualTotal;
import trap.model.RoundScore;
import trap.model.TeamScore;

import java.io.File;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void generateExcelFile() throws Exception {
        RoundScore mockScore = new RoundScore(1, "Event", 100, "Location", "Date", "Squad", "Team A", "Athlete A", "Varsity", "M", 25, 25, 0, 0, 0, 0, 0, 0, "singles");

        when(trapDataRepository.readRoundScores(anyString())).thenReturn(List.of(mockScore));

        reportService.generateExcelFile();

        verify(downloadService, times(1)).downloadFiles(any());
        verify(trapService, atLeastOnce()).calculatePlayerRoundTotals(any());

        String dateString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        File generatedFile = new File("league-data-" + dateString + ".xlsx");

        if (generatedFile.exists()) {
            assertTrue(generatedFile.delete(), "Cleanup failed: Could not delete generated file");
        }
    }

    @Test
    void csvParsingLogic() {
        // Test the parsing logic specifically using the repository refactor
        TrapDataRepository repo = new TrapDataRepository();
        String csvData = """
                Header1,Header2,Header3,Header4,Header5,Header6,Header7,Header8,Header9,Header10,Header11,Header12,Header13,Header14,Header15,Header16,Header17,Header18,Header19,Header20
                0,123,Singles,10,Ankeny,2024,Squad,Team A,Player 1,,Varsity,M,25,25,0,0,0,0,0,0""";

        StringReader stringReader = new StringReader(csvData);
        List<RoundScore> scores = repo.parseCsv(stringReader, "singles");

        assertEquals(1, scores.size());
        assertEquals("Player 1", scores.getFirst().athlete());
        assertEquals(25, scores.getFirst().round1());
    }

    @Test
    void getTeamScores_emptyInput_returnsEmptyList() {
        List<TeamScore> result = reportService.getTeamScores(List.of());

        assertTrue(result.isEmpty());
    }

    @Test
    void getTeamScores_singleTeamSinglePlayer_returnsCorrectTotal() {
        var individual = new IndividualTotal(1, "Team A", "Athlete 1", "Senior/Varsity", "M", 90, "singles");
        var entry = Map.entry("singles Team A Varsity", new ArrayList<>(List.of(individual)));

        List<TeamScore> result = reportService.getTeamScores(List.of(entry));

        assertEquals(1, result.size());
        assertEquals("Team A", result.getFirst().name());
        assertEquals(90, result.getFirst().total());
    }

    @Test
    void getTeamScores_multipleTeams_sortedByTotalDescending() {
        var individualA = new IndividualTotal(1, "Team A", "Athlete 1", "Senior/Varsity", "M", 80, "singles");
        var individualB = new IndividualTotal(2, "Team B", "Athlete 2", "Senior/Varsity", "M", 95, "singles");
        var entryA = Map.entry("singles Team A Varsity", new ArrayList<>(List.of(individualA)));
        var entryB = Map.entry("singles Team B Varsity", new ArrayList<>(List.of(individualB)));

        List<TeamScore> result = reportService.getTeamScores(List.of(entryA, entryB));

        assertEquals(2, result.size());
        assertEquals("Team B", result.get(0).name());
        assertEquals(95, result.get(0).total());
        assertEquals("Team A", result.get(1).name());
        assertEquals(80, result.get(1).total());
    }

    @Test
    void getTeamScores_singlesLimitFiveScores_onlyTopFiveCount() {
        // singles allows 5 scores to count for team total; add 7 players, only top 5 should sum
        var individuals = new ArrayList<IndividualTotal>();
        for (int i = 1; i <= 7; i++) {
            individuals.add(new IndividualTotal(i, "Team A", "Athlete " + i, "Senior/Varsity", "M", i * 10, "singles"));
        }
        // Sort descending so top 5 are scores 70,60,50,40,30 = 250
        individuals.sort((a, b) -> Integer.compare(b.total(), a.total()));
        var entry = Map.entry("singles Team A Varsity", individuals);

        List<TeamScore> result = reportService.getTeamScores(List.of(entry));

        assertEquals(1, result.size());
        assertEquals(250, result.getFirst().total()); // 70+60+50+40+30
    }

    @Test
    void getTeamScores_singlesWithTiedFifthPlace_onlyTopFiveCountInTotal() {
        // 5 regular players + 1 tied for 5th (included for display); total must only use top 5
        var individuals = new ArrayList<>(List.of(
                new IndividualTotal(1, "Team A", "Athlete 1", "Senior/Varsity", "M", 90, "singles"),
                new IndividualTotal(2, "Team A", "Athlete 2", "Senior/Varsity", "M", 80, "singles"),
                new IndividualTotal(3, "Team A", "Athlete 3", "Senior/Varsity", "M", 70, "singles"),
                new IndividualTotal(4, "Team A", "Athlete 4", "Senior/Varsity", "M", 60, "singles"),
                new IndividualTotal(5, "Team A", "Athlete 5", "Senior/Varsity", "M", 50, "singles"),
                new IndividualTotal(6, "Team A", "Athlete 6", "Senior/Varsity", "M", 50, "singles") // tied for 5th
        ));
        var entry = Map.entry("singles Team A Varsity", individuals);

        List<TeamScore> result = reportService.getTeamScores(List.of(entry));

        assertEquals(1, result.size());
        assertEquals(350, result.getFirst().total()); // 90+80+70+60+50, not 90+80+70+60+50+50
    }

    @Test
    void getTeamScores_claysLimitThreeScores_onlyTopThreeCount() {
        // clays allows 3 events to count
        var individuals = new ArrayList<IndividualTotal>();
        for (int i = 1; i <= 5; i++) {
            individuals.add(new IndividualTotal(i, "Team A", "Athlete " + i, "Senior/Varsity", "M", i * 10, "clays"));
        }
        // Sort descending so top 3 are 50,40,30 = 120
        individuals.sort((a, b) -> Integer.compare(b.total(), a.total()));
        var entry = Map.entry("clays Team A Varsity", individuals);

        List<TeamScore> result = reportService.getTeamScores(List.of(entry));

        assertEquals(1, result.size());
        assertEquals(120, result.getFirst().total()); // 50+40+30
    }

    @Test
    void getTeamScores_fiveStandLimitFiveScores_onlyTopFiveCount() {
        var individuals = new ArrayList<IndividualTotal>();
        for (int i = 1; i <= 7; i++) {
            individuals.add(new IndividualTotal(i, "Team A", "Athlete " + i, "Senior/Varsity", "M", i * 10, "fivestand"));
        }
        individuals.sort((a, b) -> Integer.compare(b.total(), a.total()));
        var entry = Map.entry("fivestand Team A Varsity", individuals);

        List<TeamScore> result = reportService.getTeamScores(List.of(entry));

        assertEquals(1, result.size());
        assertEquals(250, result.getFirst().total());
    }

    @Test
    void getTeamScores_multipleEntriesSameTeam_accumulatesTotals() {
        // Two entries for the same team (different types) should accumulate
        var singlesIndividuals = new ArrayList<>(List.of(
                new IndividualTotal(1, "Team A", "Athlete 1", "Senior/Varsity", "M", 50, "singles")
        ));
        var handicapIndividuals = new ArrayList<>(List.of(
                new IndividualTotal(2, "Team A", "Athlete 2", "Senior/Varsity", "M", 40, "handicap")
        ));
        var entryS = Map.entry("singles Team A Varsity", singlesIndividuals);
        var entryH = Map.entry("handicap Team A Varsity", handicapIndividuals);

        List<TeamScore> result = reportService.getTeamScores(List.of(entryS, entryH));

        assertEquals(1, result.size());
        assertEquals(90, result.getFirst().total());
    }

    @Test
    void calculateTeamScores_withMultiplePlayers_handlesTieForFifthPlace() {
        var individuals = new ArrayList<>(List.of(
                new IndividualTotal(1, "Team A", "Athlete 1", "Senior/Varsity", "M", 100, "singles"),
                new IndividualTotal(2, "Team A", "Athlete 2", "Senior/Varsity", "M", 90, "singles"),
                new IndividualTotal(3, "Team A", "Athlete 3", "Senior/Varsity", "M", 80, "singles"),
                new IndividualTotal(4, "Team A", "Athlete 4", "Senior/Varsity", "M", 70, "singles"),
                new IndividualTotal(5, "Team A", "Athlete 5", "Senior/Varsity", "M", 60, "singles"),
                new IndividualTotal(6, "Team A", "Athlete 6", "Senior/Varsity", "M", 60, "singles")
        ));

        List<TeamScore> result = reportService.getTeamScores(List.of(Map.entry("singles Team A Varsity", individuals)));

        assertEquals(1, result.size());
        assertEquals(400, result.getFirst().total());
    }

    @Test
    void getTeamScores_doublesScoring() {
        var individuals = new ArrayList<IndividualTotal>();
        for (int i = 1; i <= 6; i++) {
            individuals.add(new IndividualTotal(i, "Team A", "Athlete " + i, "Senior/Varsity", "M", i * 10, "doubles"));
        }
        individuals.sort((a, b) -> Integer.compare(b.total(), a.total()));
        var entry = Map.entry("doubles Team A Varsity", individuals);

        List<TeamScore> result = reportService.getTeamScores(List.of(entry));

        assertEquals(1, result.size());
        assertEquals(200, result.getFirst().total());
    }

    @Test
    void getTeamScores_handicapScoring() {
        var individuals = new ArrayList<IndividualTotal>();
        for (int i = 1; i <= 7; i++) {
            individuals.add(new IndividualTotal(i, "Team B", "Athlete " + i, "Senior/Varsity", "M", i * 10, "handicap"));
        }
        individuals.sort((a, b) -> Integer.compare(b.total(), a.total()));
        var entry = Map.entry("handicap Team B Varsity", individuals);

        List<TeamScore> result = reportService.getTeamScores(List.of(entry));

        assertEquals(1, result.size());
        assertEquals(250, result.getFirst().total());
    }

    @Test
    void getTeamScores_skeetScoring() {
        var individuals = new ArrayList<IndividualTotal>();
        for (int i = 1; i <= 5; i++) {
            individuals.add(new IndividualTotal(i, "Team C", "Athlete " + i, "Senior/Varsity", "M", i * 10, "skeet"));
        }
        individuals.sort((a, b) -> Integer.compare(b.total(), a.total()));
        var entry = Map.entry("skeet Team C Varsity", individuals);

        List<TeamScore> result = reportService.getTeamScores(List.of(entry));

        assertEquals(1, result.size());
        assertEquals(120, result.getFirst().total());
    }

    @Test
    void getTeamScores_doublesskeetScoring() {
        var individuals = new ArrayList<IndividualTotal>();
        for (int i = 1; i <= 4; i++) {
            individuals.add(new IndividualTotal(i, "Team D", "Athlete " + i, "Senior/Varsity", "M", i * 10, "doublesskeet"));
        }
        individuals.sort((a, b) -> Integer.compare(b.total(), a.total()));
        var entry = Map.entry("doublesskeet Team D Varsity", individuals);

        List<TeamScore> result = reportService.getTeamScores(List.of(entry));

        assertEquals(1, result.size());
        assertEquals(90, result.getFirst().total());
    }

    @Test
    void getTeamScores_fivestandScoring() {
        var individuals = new ArrayList<IndividualTotal>();
        for (int i = 1; i <= 8; i++) {
            individuals.add(new IndividualTotal(i, "Team E", "Athlete " + i, "Senior/Varsity", "M", i * 10, "fivestand"));
        }
        individuals.sort((a, b) -> Integer.compare(b.total(), a.total()));
        var entry = Map.entry("fivestand Team E Varsity", individuals);

        List<TeamScore> result = reportService.getTeamScores(List.of(entry));

        assertEquals(1, result.size());
        assertEquals(300, result.getFirst().total());
    }
}
