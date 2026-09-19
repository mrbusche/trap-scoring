package trap.report;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Component;
import trap.model.RoundScore;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static trap.report.TrapService.parseInteger;
import static trap.report.TrapService.setStringToZero;
import static trap.report.TrapService.trimString;

@Component
public class TrapDataRepository {
    private static final int EVENT_ID_COLUMN = 1;
    private static final int EVENT_NAME_COLUMN = 2;
    private static final int LOCATION_ID_COLUMN = 3;
    private static final int LOCATION_NAME_COLUMN = 4;
    private static final int EVENT_DATE_COLUMN = 5;
    private static final int SQUAD_NAME_COLUMN = 6;
    private static final int TEAM_NAME_COLUMN = 7;
    private static final int ATHLETE_NAME_COLUMN = 8;
    private static final int CLASSIFICATION_COLUMN = 10;
    private static final int GENDER_COLUMN = 11;
    private static final int ROUND_1_COLUMN = 12;
    private static final int ROUND_2_COLUMN = 13;
    private static final int ROUND_3_COLUMN = 14;
    private static final int ROUND_4_COLUMN = 15;
    private static final int ROUND_5_COLUMN = 16;
    private static final int ROUND_6_COLUMN = 17;
    private static final int ROUND_7_COLUMN = 18;
    private static final int ROUND_8_COLUMN = 19;

    public List<RoundScore> readRoundScores(String trapType) throws IOException {
        try (var reader = new FileReader(trapType + ".csv", StandardCharsets.UTF_8)) {
            return parseCsv(reader, trapType);
        }
    }

    public List<RoundScore> parseCsv(Reader readerSource, String type) throws IOException {
        try (var reader = new CSVReader(readerSource)) {
            try {
                var roundScores = reader.readAll();
                if (roundScores.isEmpty()) {
                    return List.of();
                }
                roundScores.removeFirst();
                return roundScores.stream()
                        .map(row -> createRoundScore(row, type))
                        .toList();
            } catch (CsvException e) {
                throw new IOException("Failed to parse CSV for " + type, e);
            }
        }
    }

    private RoundScore createRoundScore(String[] data, String type) {
        return RoundScore.builder()
                .eventId(parseInteger(valueAt(data, EVENT_ID_COLUMN)))
                .event(trimString(valueAt(data, EVENT_NAME_COLUMN)))
                .locationId(parseInteger(valueAt(data, LOCATION_ID_COLUMN)))
                .location(trimString(valueAt(data, LOCATION_NAME_COLUMN)))
                .eventDate(trimString(valueAt(data, EVENT_DATE_COLUMN)))
                .squadName(trimString(valueAt(data, SQUAD_NAME_COLUMN)))
                .team(trimString(valueAt(data, TEAM_NAME_COLUMN)).replace("Club", "Team"))
                .athlete(trimString(valueAt(data, ATHLETE_NAME_COLUMN)))
                .classification(trimString(valueAt(data, CLASSIFICATION_COLUMN)))
                .gender(trimString(valueAt(data, GENDER_COLUMN)))
                .round1(setStringToZero(valueAt(data, ROUND_1_COLUMN)))
                .round2(setStringToZero(valueAt(data, ROUND_2_COLUMN)))
                .round3(setStringToZero(valueAt(data, ROUND_3_COLUMN)))
                .round4(setStringToZero(valueAt(data, ROUND_4_COLUMN)))
                .round5(setStringToZero(valueAt(data, ROUND_5_COLUMN)))
                .round6(setStringToZero(valueAt(data, ROUND_6_COLUMN)))
                .round7(setStringToZero(valueAt(data, ROUND_7_COLUMN)))
                .round8(setStringToZero(valueAt(data, ROUND_8_COLUMN)))
                .type(type)
                .build();
    }

    private String valueAt(String[] data, int index) {
        return index < data.length ? data[index] : "";
    }
}
