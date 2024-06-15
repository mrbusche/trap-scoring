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
//        downloadHelper.downloadFiles(trapTypes);

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
        List<RoundScore> allRoundScores;
        try {
            allRoundScores = new ArrayList<>();
            for (var type : trapTypes) {
                allRoundScores.addAll(generateRoundScores(type));
            }

            return allRoundScores;
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    private Workbook getWorkbook() throws IOException {
        var in = getClass().getResourceAsStream("/main-template.xlsx");
        return WorkbookFactory.create(Objects.requireNonNull(in));
    }

    private List<RoundScore> generateRoundScores(String type) throws IOException, CsvException {
        var reader = new CSVReader(new FileReader(type + ".csv"));
        var roundScores = reader.readAll();
        roundScores.removeFirst();
        var roundScoresList = new ArrayList<RoundScore>();
        roundScores.forEach(s -> roundScoresList.add(new RoundScore(Integer.parseInt(s[1]), trimString(s[2]), Integer.parseInt(s[3]), trimString(s[4]), trimString(s[5]), trimString(s[6]), trimString(s[7]).replace("Club", "Team"), trimString(s[8]), trimString(s[10]), trimString(s[11]), setStringToZero(s[12]), setStringToZero(s[13]), setStringToZero(s[14]), setStringToZero(s[15]), setStringToZero(s[16]), setStringToZero(s[17]), setStringToZero(s[18]), setStringToZero(s[19]), type)));
        return roundScoresList;
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
            for (var indTotal : total.getValue()) {
                int currentTotal = teamTotal.getTotal();
                teamTotal.setTotal(currentTotal + indTotal.getTotal());
                teamScoresThatCount.put(indTotal.getTeam(), teamTotal);
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
        for (var total : justValues) {
            teamScoresThatCount.put(total.getTeamForScores(), new ArrayList<>());
        }

        for (var total : justValues) {
            var currentTeam = teamScoresThatCount.get(total.getTeamForScores());
            var scoresToCount = total.getType().equals(SINGLES) || total.getType().equals(HANDICAP) || total.getType().equals(DOUBLES) ? 5 : 3;
            if (currentTeam.size() < scoresToCount) {
                currentTeam.add(total);
                teamScoresThatCount.put(total.getTeamForScores(), currentTeam);
            }
        }

        return teamScoresThatCount;
    }

    private List<IndividualTotal> getTeamScoresByTotal(Map<String, IndividualTotal> allRoundScores) {
        var teamScoresByTotal = new ArrayList<>(allRoundScores.values());
        teamScoresByTotal.sort(Comparator.comparingInt(IndividualTotal::getTotal).reversed());
        return teamScoresByTotal;
    }

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

    private int setStringToZero(String number) {
        return "".equals(number) ? 0 : Integer.parseInt(number);
    }

    private String trimString(String s) {
        return s.trim();
    }

}
