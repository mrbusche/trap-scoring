package trap.report;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import trap.model.IndividualTotal;
import trap.model.RoundScore;
import trap.model.TeamScore;

import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ReportHelper {
    private static final String SINGLES = "singles";
    private static final String DOUBLES = "doubles";
    private static final String HANDICAP = "handicap";
    private static final String SKEET = "skeet";
    private static final String CLAYS = "clays";
    private static final String FIVESTAND = "fivestand";
    private static final String DOUBLESKEET = "doublesskeet";
    private static final String ROOKIE = "Rookie";
    private static final String VARSITY = "Varsity";
    private static final String INTERMEDIATE_ENTRY = "Intermediate Entry";
    private static final String JUNIOR_VARSITY = "Junior Varsity";
    private static final String INTERMEDIATE_ADVANCED = "Intermediate Advanced";
    private final String currentDate = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
    private final String[] trapTypes = new String[]{SINGLES, DOUBLES, HANDICAP, SKEET, CLAYS, FIVESTAND, DOUBLESKEET};

    TrapHelper trapHelper = new TrapHelper();
    DownloadHelper downloadHelper = new DownloadHelper();

    Logger logger = LoggerFactory.getLogger(ReportHelper.class);

    public void generateExcelFile() throws Exception {
        downloadHelper.downloadFiles(trapTypes);

        var workbook = getWorkbook();

        logger.info("Starting file creation");
        logger.info("Workbook has {} sheets", workbook.getNumberOfSheets());
        workbook.forEach(sheet -> logger.info("- {}", sheet.getSheetName()));

        long start;
        var trueStart = System.currentTimeMillis();

        var types = new HashMap<String, String>();
        types.put("Team-Senior", VARSITY);
        types.put("Team-Intermediate", INTERMEDIATE_ENTRY);
        types.put("Team-Rookie", ROOKIE);

        var mainTextStyle = ExcelHelper.getCellStyle(workbook);
        var style = ExcelHelper.setFontForHeaders(workbook);

        var allRoundScores = generateRoundScores();
        logger.info("Generated round scores in {} ms", System.currentTimeMillis() - trueStart);
        populateCleanData(workbook.getSheet("Clean Data"), allRoundScores);

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(allRoundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(allRoundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);
        var teamScoresByTotal = getTeamScoresByTotal(playerFinalTotal);
        var teamScoresThatCount = calculateTeamScores(teamScoresByTotal);

        for (Map.Entry<String, String> entry : types.entrySet()) {
            start = System.currentTimeMillis();
            populateTeamData(workbook.getSheet(entry.getKey()), entry.getValue(), mainTextStyle, teamScoresThatCount);
            logger.info("{} data populated in {} ms", entry.getKey(), System.currentTimeMillis() - start);
        }

        populateIndividualData(workbook, "Individual-Men", "M", style, mainTextStyle, playerFinalTotal);
        populateIndividualData(workbook, "Individual-Ladies", "F", style, mainTextStyle, playerFinalTotal);

        populateTeamIndividualData(workbook, "Team-Individual-Scores", teamScoresByTotal);
        populateAllIndividualData(workbook, "Individual-All-Scores", playerFinalTotal);

        ExcelHelper.createFile(workbook);

        logger.info("Finished creating file in {} ms", System.currentTimeMillis() - trueStart);
        workbook.close();
    }

    private List<RoundScore> generateRoundScores() {
        try {
            return Arrays.stream(trapTypes)
                    .flatMap(type -> {
                        try {
                            return generateRoundScores(type).stream();
                        } catch (IOException | CsvException e) {
                            throw new RuntimeException("Error generating round scores for type: " + type, e);
                        }
                    })
                    .toList();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error generating round scores", e);
        }
    }

    private Workbook getWorkbook() throws IOException {
        var in = getClass().getResourceAsStream("/main-template.xlsx");
        return WorkbookFactory.create(Objects.requireNonNull(in));
    }

    private List<RoundScore> generateRoundScores(String type) throws IOException, CsvException {
        try (var reader = new CSVReader(new FileReader(type + ".csv"))) {
            var roundScores = reader.readAll();
            if (roundScores.isEmpty()) {
                return new ArrayList<>(); // or handle as appropriate
            }
            roundScores.removeFirst(); // Remove the header row

            return roundScores.stream()
                    .map(s -> createRoundScore(s, type))
                    .toList();
        }
    }

    private RoundScore createRoundScore(String[] data, String type) {
        return new RoundScore(
                parseInteger(data[1]),
                trimString(data[2]),
                parseInteger(data[3]),
                trimString(data[4]),
                trimString(data[5]),
                trimString(data[6]),
                trimString(data[7]).replace("Club", "Team"),
                trimString(data[8]),
                trimString(data[10]),
                trimString(data[11]),
                setStringToZero(data[12]),
                setStringToZero(data[13]),
                setStringToZero(data[14]),
                setStringToZero(data[15]),
                setStringToZero(data[16]),
                setStringToZero(data[17]),
                setStringToZero(data[18]),
                setStringToZero(data[19]),
                type
        );
    }

    private void populateCleanData(Sheet sheet, List<RoundScore> allRoundScores) {
        var start = System.currentTimeMillis();

        logger.info("Ran get all data for clean data population in {} ms", System.currentTimeMillis() - start);

        var rows = sheet.getLastRowNum();

        for (var type : trapTypes) {
            var typeStart = System.currentTimeMillis();
            var typeRoundScores = allRoundScores.stream().filter(t -> t.getType().equals(type)).toList();
            for (var score : typeRoundScores) {
                var row = sheet.createRow(++rows);
                ExcelHelper.addCleanData(row, score);
            }
            logger.info("Clean data for {} {} scores populated in {} ms", typeRoundScores.size(), type, System.currentTimeMillis() - typeStart);
        }

        sheet.setAutoFilter(CellRangeAddress.valueOf("A1:S1"));
        logger.info("Clean data populated in {} ms", System.currentTimeMillis() - start);
    }

    private List<TeamScore> getTeamScores(List<Map.Entry<String, ArrayList<IndividualTotal>>> teamData) {
        var teamScoresThatCount = new HashMap<String, TeamScore>();
        for (Map.Entry<String, ArrayList<IndividualTotal>> total : teamData) {
            var details = new TeamScore(total.getValue().getFirst().getTeam(), 0);
            teamScoresThatCount.put(total.getValue().getFirst().getTeam(), details);
        }

        for (Map.Entry<String, ArrayList<IndividualTotal>> total : teamData) {
            var teamTotal = teamScoresThatCount.get(total.getValue().getFirst().getTeam());
            var scoresCounted = 0;
            int scoresToCount = total.getValue().getFirst().getType().equals(SINGLES) || total.getValue().getFirst().getType().equals(HANDICAP) || total.getValue().getFirst().getType().equals(DOUBLES) ? 5 : 3;
            ;
            for (var indTotal : total.getValue()) {
                int currentTotal = teamTotal.getTotal();
                teamTotal.setTotal(currentTotal + indTotal.getTotal());
                teamScoresThatCount.put(indTotal.getTeam(), teamTotal);
                scoresCounted++;
                if (scoresCounted >= scoresToCount) {
                    break;
                }
            }
        }

        return teamScoresThatCount.values().stream().sorted(Comparator.comparingInt(TeamScore::getTotal).reversed()).toList();
    }

    private void populateTeamData(Sheet sheet, String teamType, CellStyle mainTextStyle, HashMap<String, ArrayList<IndividualTotal>> teamScoresByTotal) {
        ExcelHelper.setCurrentDateHeader(sheet, currentDate);
        ExcelHelper.setCurrentSeasonHeader(sheet);

        var rows = sheet.getLastRowNum();
        Row row;

        int updateRow = rows;
        int startColumn = 1;
        long start = System.currentTimeMillis();

        List<Map.Entry<String, ArrayList<IndividualTotal>>> teamData = teamScoresByTotal.entrySet().stream().filter(f -> f.getValue().getFirst().getTeamClassificationForTotal().equals(teamType) && f.getValue().getFirst().getType().equals(SINGLES)).toList();
        List<TeamScore> teamScores = getTeamScores(teamData);
        logger.info("Ran query for singles by {} in {} ms", teamType, System.currentTimeMillis() - start);
        for (var teamScore : teamScores) {
            row = sheet.createRow(++updateRow);
            ExcelHelper.addTeamData(row, startColumn, teamScore.getName(), teamScore.getTotal(), mainTextStyle);
        }

        if (!ROOKIE.equals(teamType)) {
            startColumn += 3;

            var types = new String[]{HANDICAP, DOUBLES, SKEET, CLAYS, FIVESTAND, DOUBLESKEET};
            for (var type : types) {
                updateRow = rows;
                start = System.currentTimeMillis();
                teamData = teamScoresByTotal.entrySet().stream().filter(f -> f.getValue().getFirst().getTeamClassificationForTotal().equals(teamType) && f.getValue().getFirst().getType().equals(type)).toList();
                teamScores = getTeamScores(teamData);
                logger.info("Ran query for {} by {} in {} ms", type, teamType, System.currentTimeMillis() - start);
                for (var teamScore : teamScores) {
                    row = sheet.getRow(++updateRow);
                    ExcelHelper.addTeamData(row, startColumn, teamScore.getName(), teamScore.getTotal(), mainTextStyle);
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
        var classificationList = Arrays.asList(VARSITY, JUNIOR_VARSITY, INTERMEDIATE_ADVANCED, INTERMEDIATE_ENTRY, ROOKIE);
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
            justValues.sort(Comparator.comparingInt(IndividualTotal::getTotal).reversed());

            var individualData = justValues.stream().filter(f -> f.getGender().equals(gender) && f.getTeamClassification().equals(classification) && f.getType().equals(SINGLES)).toList();
            logger.info("Ran query for singles by {} and {} in {} ms", gender, classification, System.currentTimeMillis() - start);

            for (IndividualTotal singlesRowData : individualData) {
                row = sheet.createRow(++updateRow);
                ExcelHelper.addPlayerData(row, column, singlesRowData.getAthlete(), singlesRowData.getTotal(), singlesRowData.getTeam(), mainTextStyle);
            }
            column += 4;
            maxRow = Math.max(maxRow, updateRow);

            String[] types = new String[]{HANDICAP, DOUBLES, SKEET, CLAYS, FIVESTAND, DOUBLESKEET};
            for (String type : types) {
                updateRow = classificationStartRow;
                updateRow++;
                start = System.currentTimeMillis();
                individualData = justValues.stream().filter(f -> f.getGender().equals(gender) && f.getTeamClassification().equals(classification) && f.getType().equals(type)).toList();
                logger.info("Ran query for {} by {} and {} in {} ms", type, gender, classification, System.currentTimeMillis() - start);
                for (IndividualTotal data : individualData) {
                    row = sheet.getRow(++updateRow);
                    ExcelHelper.addPlayerData(row, column, data.getAthlete(), data.getTotal(), data.getTeam(), mainTextStyle);
                }
                maxRow = Math.max(maxRow, updateRow);
                column += 4;
            }

            sheet.setAutoFilter(CellRangeAddress.valueOf("A13:AB13"));
        }
        logger.info("{} data populated in {} ms", sheetName, System.currentTimeMillis() - initialStart);
    }

    private HashMap<String, ArrayList<IndividualTotal>> calculateTeamScores(List<IndividualTotal> justValues) {
        var teamScoresThatCount = new HashMap<String, ArrayList<IndividualTotal>>();

        // Group scores by team
        for (var total : justValues) {
            teamScoresThatCount.putIfAbsent(total.getTeamForScores(), new ArrayList<>());
        }

        // Add totals to respective teams
        for (var total : justValues) {
            var currentTeam = teamScoresThatCount.get(total.getTeamForScores());
            currentTeam.add(total);
        }

        // Process each team to handle ties for 5th place
        for (var team : teamScoresThatCount.keySet()) {
            var currentTeam = teamScoresThatCount.get(team);

            // Sort the team scores in descending order based on the 'total' field
            currentTeam.sort((a, b) -> Integer.compare(b.getTotal(), a.getTotal()));

            // Handle ties at 5th place
            int scoresToCount = currentTeam.getFirst().getType().equals(SINGLES) || currentTeam.getFirst().getType().equals(HANDICAP) || currentTeam.getFirst().getType().equals(DOUBLES) ? 5 : 3;
            ;
            if (currentTeam.size() > scoresToCount) {
                int fifthScore = currentTeam.get(scoresToCount - 1).getTotal();

                // Include all individuals tied with the 5th score
                for (int i = scoresToCount; i < currentTeam.size(); i++) {
                    if (currentTeam.get(i).getTotal() != fifthScore) {
                        currentTeam.subList(i, currentTeam.size()).clear(); // Remove the rest
                        break;
                    }
                }
            }
        }

        return teamScoresThatCount;
    }

    private List<IndividualTotal> getTeamScoresByTotal(Map<String, IndividualTotal> allRoundScores) {
        var teamScoresByTotal = new ArrayList<>(allRoundScores.values());
        teamScoresByTotal.sort(Comparator.comparingInt(IndividualTotal::getTotal).reversed());
        return teamScoresByTotal;
    }

    // Team-Individual-Scores tab
    private void populateTeamIndividualData(Workbook workbook, String sheetName, List<IndividualTotal> teamScoresByTotal) {
        var sheet = workbook.getSheet(sheetName);
        var startTime = System.currentTimeMillis();
        var start = System.currentTimeMillis();
        logger.info("Ran query for team scores in {} ms", System.currentTimeMillis() - start);

        var rows = sheet.getLastRowNum();
        var teamScoresThatCount = calculateTeamScores(teamScoresByTotal);
        for (var individualTotalList : teamScoresThatCount.values()) {
            for (var rowData : individualTotalList) {
                rows = ExcelHelper.getRows(sheet, rows, rowData);
            }
        }

        sheet.setAutoFilter(CellRangeAddress.valueOf("A1:E1"));
        logger.info("Team Individual Scores data populated in {} ms", System.currentTimeMillis() - startTime);
    }

    private void populateAllIndividualData(Workbook workbook, String sheetName, Map<String, IndividualTotal> allRoundScores) {
        var sheet = workbook.getSheet(sheetName);
        var trueStart = System.currentTimeMillis();
        var start = System.currentTimeMillis();

        var justValues = new ArrayList<>(allRoundScores.values());
        justValues.sort(Comparator.comparing(IndividualTotal::getTeam));
        logger.info("Ran query for all scores in {} ms", System.currentTimeMillis() - start);

        var rows = sheet.getLastRowNum();

        Cell cell;
        Row row;
        for (var rowData : justValues) {
            row = sheet.createRow(++rows);
            cell = row.createCell(0);
            ExcelHelper.generateTeamRows(rowData, row, cell);
            cell = row.createCell(5);
            cell.setCellValue(rowData.getGender());
        }
        sheet.setAutoFilter(CellRangeAddress.valueOf("A1:F1"));
        logger.info("Individual All Scores data populated in {} ms", System.currentTimeMillis() - trueStart);
    }

    private int parseInteger(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int setStringToZero(String number) {
        return number.isEmpty() ? 0 : parseInteger(number);
    }

    private String trimString(String s) {
        return s.trim();
    }

}
