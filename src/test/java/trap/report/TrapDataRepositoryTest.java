package trap.report;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import trap.model.RoundScore;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

class TrapDataRepositoryTest {
    private final TrapDataRepository repository = new TrapDataRepository();

    @Test
    void readsAndMapsCsvFile(@TempDir Path tempDir) throws Exception {
        String type = tempDir.resolve("singles").toString();
        Files.writeString(Path.of(type + ".csv"), """
                Header
                0,123,Singles,10,Ankeny,2024,Squad,Team Club,Player,,Varsity,M,25,24,23,22,21,20,19,18
                """);

        RoundScore score = repository.readRoundScores(type).getFirst();

        assertEquals(123, score.eventId());
        assertEquals("Singles", score.event());
        assertEquals(10, score.locationId());
        assertEquals("Team Team", score.team());
        assertEquals("Player", score.athlete());
        assertEquals(25, score.round1());
        assertEquals(18, score.round8());
        assertEquals(type, score.type());
    }

    @Test
    void returnsEmptyListForEmptyAndHeaderOnlyCsv() throws Exception {
        assertTrue(repository.parseCsv(new StringReader(""), "singles").isEmpty());
        assertTrue(repository.parseCsv(new StringReader("Header\n"), "singles").isEmpty());
    }

    @Test
    void fillsMissingColumnsWithEmptyValues() throws Exception {
        RoundScore score = repository.parseCsv(new StringReader("Header\n0,1,Event"), "singles").getFirst();

        assertEquals(1, score.eventId());
        assertEquals("Event", score.event());
        assertEquals("", score.location());
        assertEquals("", score.team());
        assertEquals(0, score.round1());
    }

    @Test
    void wrapsCsvExceptionsWithTypeContext() {
        var csvException = new CsvException("malformed csv");
        try (var ignored = mockConstruction(CSVReader.class, (mock, context) ->
                when(mock.readAll()).thenThrow(csvException))) {
            IOException exception = assertThrows(IOException.class,
                    () -> repository.parseCsv(new StringReader("Header"), "singles"));

            assertEquals("Failed to parse CSV for singles", exception.getMessage());
            assertEquals(csvException, exception.getCause());
        }
    }
}
