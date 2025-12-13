package trap.report;

import com.opencsv.CSVReader;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import trap.model.RoundScore;

import java.io.FileReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static trap.report.TrapService.parseInteger;
import static trap.report.TrapService.setStringToZero;
import static trap.report.TrapService.trimString;

@Component
public class TrapDataRepository {

    @SneakyThrows
    public List<RoundScore> readRoundScores(String trapType) {
        try (var reader = new FileReader(trapType + ".csv", StandardCharsets.UTF_8)) {
            return parseCsv(reader, trapType);
        }
    }

    @SneakyThrows
    public List<RoundScore> parseCsv(Reader readerSource, String type) {
        try (var reader = new CSVReader(readerSource)) {
            var roundScores = reader.readAll();
            if (roundScores.isEmpty()) {
                return List.of();
            }
            roundScores.removeFirst(); // Remove header row
            return roundScores.stream().map(s -> createRoundScore(s, type)).toList();
        }
    }

    private RoundScore createRoundScore(String[] data, String type) {
        return RoundScore.builder().eventId(parseInteger(data[1])).event(trimString(data[2])).locationId(parseInteger(data[3])).location(trimString(data[4])).eventDate(trimString(data[5])).squadName(trimString(data[6])).team(trimString(data[7]).replace("Club", "Team")).athlete(trimString(data[8])).classification(trimString(data[10])).gender(trimString(data[11])).round1(setStringToZero(data[12])).round2(setStringToZero(data[13])).round3(setStringToZero(data[14])).round4(setStringToZero(data[15])).round5(setStringToZero(data[16])).round6(setStringToZero(data[17])).round7(setStringToZero(data[18])).round8(setStringToZero(data[19])).type(type).build();
    }
}
