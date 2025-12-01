package trap.report;

import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;
import trap.common.Classifications;
import trap.common.EventTypes;
import trap.model.IndividualTotal;
import trap.model.RoundScore;
import trap.model.TeamScore;
import trap.model.TrapRoundScore;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static trap.report.TrapService.getRoundsToCount;
import static trap.report.TrapService.parseInteger;
import static trap.report.TrapService.setStringToZero;
import static trap.report.TrapService.trimString;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {
    private final String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    private final String[] trapTypes = new String[]{EventTypes.SINGLES, EventTypes.DOUBLES, EventTypes.HANDICAP, EventTypes.SKEET, EventTypes.CLAYS, EventTypes.FIVESTAND, EventTypes.DOUBLESKEET};

    private final DownloadService downloadService;
    private final TrapService trapService;

    public void generateExcelFile() throws Exception {
        downloadService.downloadFiles(trapTypes);

        var workbook = getWorkbook();

        log.info("Starting file creation");
        log.info("Workbook has {} sheets", workbook.getNumberOfSheets());
        workbook.forEach(sheet -> log.info("- {}", sheet.getSheetName()));

        long start;
        var trueStart = System.currentTimeMillis();

        var types = Map.of(
                "Team-Senior", Classifications.VARSITY,
                "Team-Intermediate", Classifications.INTERMEDIATE_ENTRY,
                "Team-Rookie", Classifications.ROOKIE
        );

        var mainTextStyle = ExcelHelper.getCellStyle(workbook);
        var style = ExcelHelper.setFontForHeaders(workbook);

        var allRoundScores = generateRoundScores();
        log.info("Generated round scores in {} ms", System.currentTimeMillis() - trueStart);
        populateCleanData(workbook.getSheet("Clean Data"), allRoundScores);

        var trapRoundScores = allRoundScores.stream().map(TrapRoundScore::new).toList();
        var playerRoundTotals = trapService.calculatePlayerRoundTotals(trapRoundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(trapRoundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);
        var teamScoresByTotal = getTeamScoresByTotal(playerFinalTotal);
        var teamScoresThatCount = calculateTeamScores(teamScoresByTotal);

        for (Map.Entry<String, String> entry : types.entrySet()) {
            start = System.currentTimeMillis();
            populateTeamData(workbook.getSheet(entry.getKey()), entry.getValue(), mainTextStyle, teamScoresThatCount);
            log.info("{} data populated in {} ms", entry.getKey(), System.currentTimeMillis() - start);
        }

        populateIndividualData(workbook, "Individual-Men", "M", style, mainTextStyle, playerFinalTotal);
        populateIndividualData(workbook, "Individual-Ladies", "F", style, mainTextStyle, playerFinalTotal);

        populateTeamIndividualData(workbook, teamScoresByTotal);
        populateAllIndividualData(workbook, playerFinalTotal);

        ExcelHelper.createFile(workbook);

        log.info("Finished creating file in {} ms", System.currentTimeMillis() - trueStart);
        workbook.close();
    }

    private List<RoundScore> generateRoundScores() {
        return Arrays.stream(trapTypes)
                .flatMap(type -> generateRoundScores(type).stream())
                .toList();
    }

    private Workbook getWorkbook() throws IOException {
        var in = getClass().getResourceAsStream("/main-template.xlsx");
        return WorkbookFactory.create(Objects.requireNonNull(in));
    }

    @SneakyThrows
    private List<RoundScore> generateRoundScores(String type) {
        try (var reader = new CSVReader(new FileReader(type + ".csv", StandardCharsets.UTF_8))) {
            var roundScores = reader.readAll();
            if (roundScores.isEmpty()) {
                return new ArrayList<>(); // or handle as appropriate
            }
            roundScores.removeFirst(); // Remove the header row

            return roundScores.stream().map(s -> createRoundScore(s, type)).toList();
        }
    }

    private RoundScore createRoundScore(String[] data, String type) {
        return RoundScore.builder()
                .eventId(parseInteger(data[1]))
                .event(trimString(data[2]))
                .locationId(parseInteger(data[3]))
                .location(trimString(data[4]))
                .eventDate(trimString(data[5]))
                .squadName(trimString(data[6]))
                .team(trimString(data[7]).replace("Club", "Team"))
                .athlete(trimString(data[8]))
                .classification(trimString(data[10]))
                .gender(trimString(data[11]))
                .round1(setStringToZero(data[12]))
                .round2(setStringToZero(data[13]))
                .round3(setStringToZero(data[14]))
                .round4(setStringToZero(data[15]))
                .round5(setStringToZero(data[16]))
                .round6(setStringToZero(data[17]))
                .round7(setStringToZero(data[18]))
                .round8(setStringToZero(data[19]))
                .type(type)
                .build();
    }

    private void populateCleanData(Sheet sheet, List<RoundScore> allRoundScores) {
        var start = System.currentTimeMillis();

        log.info("Ran get all data for clean data population in {} ms", System.currentTimeMillis() - start);

        var rows = new AtomicInteger(sheet.getLastRowNum());
        Map<String, List<RoundScore>> scoresByType = allRoundScores.stream()
                .collect(Collectors.groupingBy(RoundScore::type));

        scoresByType.forEach((type, scores) -> {
            var typeStart = System.currentTimeMillis();
            for (var score : scores) {
                var row = sheet.createRow(rows.incrementAndGet());
                ExcelHelper.addCleanData(row, score);
            }
            log.info("Clean data for {} {} scores populated in {} ms", scores.size(), type, System.currentTimeMillis() - typeStart);
        });

        sheet.setAutoFilter(CellRangeAddress.valueOf("A1:S1"));
        log.info("Clean data populated in {} ms", System.currentTimeMillis() - start);
    }

    private List<TeamScore> getTeamScores(List<Map.Entry<String, ArrayList<IndividualTotal>>> teamData) {
        var teamScoresThatCount = new HashMap<String, TeamScore>();

        for (Map.Entry<String, ArrayList<IndividualTotal>> entry : teamData) {
            var team = entry.getValue().getFirst().team();
            teamScoresThatCount.putIfAbsent(team, new TeamScore(team, 0));

            // Get the first individual's team and type
            IndividualTotal firstIndividual = entry.getValue().getFirst();
            var teamName = firstIndividual.team();

            // Determine the number of scores to count based on the type
            int scoresToCount = getRoundsToCount(firstIndividual.type());

            // Sum the top scores up to the limit (scoresToCount)
            int scoreSum = entry.getValue().stream().limit(scoresToCount).mapToInt(IndividualTotal::total).sum();

            // Update the team's total score by creating a new TeamScore instance
            teamScoresThatCount.computeIfPresent(teamName, (_, currentTeamScore) ->
                    currentTeamScore.withTotal(currentTeamScore.total() + scoreSum));
        }

        return teamScoresThatCount.values().stream().sorted(Comparator.comparingInt(TeamScore::total).reversed()).toList();
    }

    private void populateTeamData(Sheet sheet, String teamType, CellStyle mainTextStyle, HashMap<String, ArrayList<IndividualTotal>> teamScoresByTotal) {
        ExcelHelper.setCurrentDateHeader(sheet, currentDate);
        ExcelHelper.setCurrentSeasonHeader(sheet);

        var rows = sheet.getLastRowNum();
        Row row;

        int updateRow = rows;
        int startColumn = 1;
        long start = System.currentTimeMillis();

        List<Map.Entry<String, ArrayList<IndividualTotal>>> teamData = teamScoresByTotal.entrySet().stream().filter(f -> f.getValue().getFirst().teamClassificationForTotal().equals(teamType) && f.getValue().getFirst().type().equals(EventTypes.SINGLES)).toList();
        List<TeamScore> teamScores = getTeamScores(teamData);
        log.info("Ran query for singles by {} in {} ms", teamType, System.currentTimeMillis() - start);
        for (var teamScore : teamScores) {
            row = sheet.createRow(++updateRow);
            ExcelHelper.addTeamData(row, startColumn, teamScore.name(), teamScore.total(), mainTextStyle);
        }

        if (!Classifications.ROOKIE.equals(teamType)) {
            startColumn += 3;

            var types = new String[]{EventTypes.HANDICAP, EventTypes.DOUBLES, EventTypes.SKEET, EventTypes.CLAYS, EventTypes.FIVESTAND, EventTypes.DOUBLESKEET};
            for (var type : types) {
                updateRow = rows;
                start = System.currentTimeMillis();
                teamData = teamScoresByTotal.entrySet().stream().filter(f -> f.getValue().getFirst().teamClassificationForTotal().equals(teamType) && f.getValue().getFirst().type().equals(type)).toList();
                teamScores = getTeamScores(teamData);
                log.info("Ran query for {} by {} in {} ms", type, teamType, System.currentTimeMillis() - start);
                for (var teamScore : teamScores) {
                    row = sheet.getRow(++updateRow);
                    ExcelHelper.addTeamData(row, startColumn, teamScore.name(), teamScore.total(), mainTextStyle);
                }
                startColumn += 3;
            }

        }
    }

    private void populateIndividualData(Workbook workbook, String sheetName, String gender, CellStyle style, CellStyle mainTextStyle, Map<String, IndividualTotal> allRoundScores) {
        var initialStart = System.currentTimeMillis();
        var sheet = workbook.getSheet(sheetName);
        ExcelHelper.setCurrentDateHeader(sheet, currentDate);
        ExcelHelper.setCurrentSeasonHeader(sheet);

        var rows = sheet.getLastRowNum();
        Cell cell;
        Row row;

        int updateRow;
        int maxRow = rows;
        int classificationStartRow;
        var addBlankRowForHeader = false;
        var classificationList = Arrays.asList(Classifications.VARSITY, Classifications.JUNIOR_VARSITY, Classifications.INTERMEDIATE_ADVANCED, Classifications.INTERMEDIATE_ENTRY, Classifications.ROOKIE);
        long start;
        for (var classification : classificationList) {
            int column = 1;
            updateRow = maxRow;
            //Add blank row
            if (addBlankRowForHeader) {
                row = sheet.createRow(++updateRow);
                cell = row.createCell(column);
                cell.setCellValue("");
            }
            addBlankRowForHeader = true;
            classificationStartRow = updateRow;
            //Add row headers
            row = sheet.createRow(++updateRow);
            cell = row.createCell(column);
            cell.setCellValue(classification);
            cell.setCellStyle(style);
            cell = row.createCell(column + 4);
            cell.setCellValue(classification);
            cell.setCellStyle(style);
            cell = row.createCell(column + 8);
            cell.setCellValue(classification);
            cell.setCellStyle(style);
            cell = row.createCell(column + 12);
            cell.setCellValue(classification);
            cell.setCellStyle(style);
            cell = row.createCell(column + 16);
            cell.setCellValue(classification);
            cell.setCellStyle(style);

            start = System.currentTimeMillis();

            List<IndividualTotal> justValues = new ArrayList<>(allRoundScores.values());
            justValues.sort(Comparator.comparingInt(IndividualTotal::total).reversed());

            var individualData = justValues.stream().filter(f -> f.gender().equals(gender) && f.teamClassification().equals(classification) && f.type().equals(EventTypes.SINGLES)).toList();
            log.info("Ran query for singles by {} and {} in {} ms", gender, classification, System.currentTimeMillis() - start);

            for (IndividualTotal singlesRowData : individualData) {
                row = sheet.createRow(++updateRow);
                ExcelHelper.addPlayerData(row, column, singlesRowData.athlete(), singlesRowData.total(), singlesRowData.team(), mainTextStyle);
            }
            column += 4;
            maxRow = Math.max(maxRow, updateRow);

            String[] types = new String[]{EventTypes.HANDICAP, EventTypes.DOUBLES, EventTypes.SKEET, EventTypes.CLAYS, EventTypes.FIVESTAND, EventTypes.DOUBLESKEET};
            for (String type : types) {
                updateRow = classificationStartRow;
                updateRow++;
                start = System.currentTimeMillis();
                individualData = justValues.stream().filter(f -> f.gender().equals(gender) && f.teamClassification().equals(classification) && f.type().equals(type)).toList();
                log.info("Ran query for {} by {} and {} in {} ms", type, gender, classification, System.currentTimeMillis() - start);
                for (IndividualTotal data : individualData) {
                    row = sheet.getRow(++updateRow);
                    ExcelHelper.addPlayerData(row, column, data.athlete(), data.total(), data.team(), mainTextStyle);
                }
                maxRow = Math.max(maxRow, updateRow);
                column += 4;
            }

            sheet.setAutoFilter(CellRangeAddress.valueOf("A13:AB13"));
        }
        log.info("{} data populated in {} ms", sheetName, System.currentTimeMillis() - initialStart);
    }

    private HashMap<String, ArrayList<IndividualTotal>> calculateTeamScores(List<IndividualTotal> justValues) {
        var teamScoresThatCount = new HashMap<String, ArrayList<IndividualTotal>>();

        // Group scores by team
        for (var total : justValues) {
            teamScoresThatCount.putIfAbsent(total.teamForScores(), new ArrayList<>());
        }

        // Add totals to respective teams
        for (var total : justValues) {
            var currentTeam = teamScoresThatCount.get(total.teamForScores());
            currentTeam.add(total);
        }

        // Process each team to handle ties for 5th place
        for (var team : teamScoresThatCount.entrySet()) {
            var currentTeam = team.getValue();

            // Sort the team scores in descending order based on the 'total' field
            currentTeam.sort((a, b) -> Integer.compare(b.total(), a.total()));

            // Handle ties at 5th place
            int scoresToCount = getRoundsToCount(currentTeam.getFirst().type());
            if (currentTeam.size() > scoresToCount) {
                int fifthScore = currentTeam.get(scoresToCount - 1).total();

                // Include all individuals tied with the 5th score
                for (int i = scoresToCount; i < currentTeam.size(); i++) {
                    if (currentTeam.get(i).total() != fifthScore) {
                        currentTeam.subList(i, currentTeam.size()).clear(); // Remove the rest
                        break;
                    }
                }
            }
        }

        return teamScoresThatCount;
    }

    private List<IndividualTotal> getTeamScoresByTotal(Map<String, IndividualTotal> allRoundScores) {
        return allRoundScores.values().stream().sorted(Comparator.comparingInt(IndividualTotal::total).reversed()).toList();
    }

    // Team-Individual-Scores tab
    private void populateTeamIndividualData(Workbook workbook, List<IndividualTotal> teamScoresByTotal) {
        var sheet = workbook.getSheet("Team-Individual-Scores");
        var startTime = System.currentTimeMillis();

        log.info("Ran query for team scores in {} ms", System.currentTimeMillis() - startTime);

        var rows = sheet.getLastRowNum();
        var teamScoresThatCount = calculateTeamScores(teamScoresByTotal);
        for (var individualTotalList : teamScoresThatCount.values()) {
            for (var rowData : individualTotalList) {
                rows = ExcelHelper.getRows(sheet, rows, rowData);
            }
        }

        sheet.setAutoFilter(CellRangeAddress.valueOf("A1:E1"));

        log.info("Team Individual Scores data populated in {} ms", System.currentTimeMillis() - startTime);
    }

    private void populateAllIndividualData(Workbook workbook, Map<String, IndividualTotal> allRoundScores) {
        var sheet = workbook.getSheet("Individual-All-Scores");
        var start = System.currentTimeMillis();

        var sortedValues = allRoundScores.values().stream().sorted(Comparator.comparing(IndividualTotal::team)).toList();
        log.info("Ran query for all scores in {} ms", System.currentTimeMillis() - start);

        var rows = sheet.getLastRowNum();

        for (var rowData : sortedValues) {
            Row row = sheet.createRow(++rows);
            ExcelHelper.generateTeamRows(rowData, row);
            row.createCell(5).setCellValue(rowData.gender());
        }
        sheet.setAutoFilter(CellRangeAddress.valueOf("A1:F1"));
        log.info("Individual All Scores data populated in {} ms", System.currentTimeMillis() - start);
    }
}
