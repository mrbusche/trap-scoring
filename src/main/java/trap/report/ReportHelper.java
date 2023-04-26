package trap.report;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;
import trap.model.IndividualTotal;
import trap.model.RoundScore;
import trap.model.TeamScore;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ReportHelper {
    private static final String COMMA_DELIMITER = ",";
    private final String currentDate = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
    private final String[] trapTypes = new String[]{"singles", "doubles", "handicap", "skeet", "clays", "fivestand", "doublesskeet"};

    TrapHelper trapHelper = new TrapHelper();
    DownloadHelper downloadHelper = new DownloadHelper();
    ExcelHelper excelHelper = new ExcelHelper();

    public void doItAll() throws Exception {
        downloadHelper.downloadFiles(trapTypes);

        Workbook workbook = getWorkbook("main");

        System.out.println("Starting file creation");
        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " sheets");
        workbook.forEach(sheet -> System.out.println("- " + sheet.getSheetName()));

        long start;
        long trueStart = System.currentTimeMillis();

        Map<String, String> types = new HashMap<>();
        types.put("Team-Senior", "Varsity");
        types.put("Team-Intermediate", "Intermediate Entry");
        types.put("Team-Rookie", "Rookie");

        CellStyle mainTextStyle = excelHelper.getCellStyle(workbook);
        CellStyle style = excelHelper.setFontForHeaders(workbook);

        List<RoundScore> allRoundScores = generateRoundScores();
        populateCleanData(workbook.getSheet("Clean Data"), allRoundScores);

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(allRoundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(allRoundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);
        var teamScoresByTotal = getTeamScoresByTotal(playerFinalTotal);
        var teamScoresThatCount = calculateTeamScores(teamScoresByTotal);

        for (Map.Entry<String, String> entry : types.entrySet()) {
            start = System.currentTimeMillis();
            populateTeamData(workbook.getSheet(entry.getKey()), entry.getValue(), mainTextStyle, teamScoresThatCount);
            System.out.println(entry.getKey() + " data populated in " + (System.currentTimeMillis() - start) + "ms");
        }

        populateIndividualData(workbook, "Individual-Men", "M", style, mainTextStyle, playerFinalTotal);
        populateIndividualData(workbook, "Individual-Ladies", "F", style, mainTextStyle, playerFinalTotal);

        populateTeamIndividualData(workbook, "Team-Individual-Scores", teamScoresByTotal);
        populateAllIndividualData(workbook, "Individual-All-Scores", playerFinalTotal);

        excelHelper.createFile(workbook, "league-data");

        System.out.println("Finished creating file in " + (System.currentTimeMillis() - trueStart) + "ms");
        workbook.close();
    }

    private List<RoundScore> generateRoundScores() {
        List<RoundScore> allRoundScores;
        try {
            List<RoundScore> singles = generateRoundScores("singles");
            List<RoundScore> doubles = generateRoundScores("doubles");
            List<RoundScore> handicap = generateRoundScores("handicap");
            List<RoundScore> skeet = generateRoundScores("skeet");
            List<RoundScore> clays = generateRoundScores("clays");
            List<RoundScore> fivestand = generateRoundScores("fivestand");
            List<RoundScore> doublesskeet = generateRoundScores("doublesskeet");

            allRoundScores = new ArrayList<>(singles);
            allRoundScores.addAll(doubles);
            allRoundScores.addAll(handicap);
            allRoundScores.addAll(skeet);
            allRoundScores.addAll(clays);
            allRoundScores.addAll(fivestand);
            allRoundScores.addAll(doublesskeet);
            return allRoundScores;
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    private Workbook getWorkbook(String templateName) throws IOException {
        InputStream in = getClass().getResourceAsStream("/" + templateName + "-template.xlsx");
        return WorkbookFactory.create(Objects.requireNonNull(in));
    }

    private List<RoundScore> generateRoundScores(String type) throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader(type + ".csv"));
        List<String[]> roundScores = reader.readAll();
        roundScores.remove(0);
        List<RoundScore> roundScoresList = new ArrayList<>();
        roundScores.forEach((s) -> roundScoresList.add(new RoundScore(Integer.parseInt(s[1]), s[2], Integer.parseInt(s[3]), s[4], s[5], s[6], s[7].replace("Club", "Team"), s[8], s[10].replace("Senior/Varsity", "Varsity").replace("Senior/Jr. Varsity", "Junior Varsity").replace("Intermediate/Advanced", "Intermediate Advanced").replace("Intermediate/Entry Level", "Intermediate Entry"), s[11], "".equals(s[12]) ? 0 : Integer.parseInt(s[12]), "".equals(s[13]) ? 0 : Integer.parseInt(s[13]), "".equals(s[14]) ? 0 : Integer.parseInt(s[14]), "".equals(s[15]) ? 0 : Integer.parseInt(s[15]), "".equals(s[16]) ? 0 : Integer.parseInt(s[16]), "".equals(s[17]) ? 0 : Integer.parseInt(s[17]), "".equals(s[18]) ? 0 : Integer.parseInt(s[18]), "".equals(s[19]) ? 0 : Integer.parseInt(s[19]), type)));
        return roundScoresList;
    }

    private void populateCleanData(Sheet sheet, List<RoundScore> allRoundScores) {
        long start = System.currentTimeMillis();

        System.out.println("Ran get all data for clean data population " + (System.currentTimeMillis() - start) + "ms");

        int rows = sheet.getLastRowNum();
        Row row;

        for (String type : trapTypes) {
            List<RoundScore> typeRoundScores = allRoundScores.stream().filter(t -> t.getType().equals(type)).toList();
            for (RoundScore record : typeRoundScores) {
                row = sheet.createRow(++rows);
                addCleanData(row, record, type);
            }
        }

        sheet.setAutoFilter(CellRangeAddress.valueOf("A1:S1"));
        System.out.println("Clean data populated in " + (System.currentTimeMillis() - start) + "ms");
    }

    private void addCleanData(Row row, RoundScore rowData, String type) {
        Cell cell;
        try {
            cell = row.createCell(0);
            cell.setCellValue(rowData.getEventId());
            cell = row.createCell(1);
            cell.setCellValue(rowData.getEvent());
            cell = row.createCell(2);
            cell.setCellValue(rowData.getLocationId());
            cell = row.createCell(3);
            cell.setCellValue(rowData.getLocation());
            cell = row.createCell(4);
            cell.setCellValue(rowData.getEventDate());
            cell = row.createCell(5);
            cell.setCellValue(rowData.getSquadName());
            cell = row.createCell(6);
            cell.setCellValue(rowData.getTeam());
            cell = row.createCell(7);
            cell.setCellValue(rowData.getAthlete());
            cell = row.createCell(8);
            cell.setCellValue(rowData.getClassification());
            cell = row.createCell(9);
            cell.setCellValue(rowData.getGender());
            cell = row.createCell(10);
            cell.setCellValue(rowData.getRound1());
            cell = row.createCell(11);
            cell.setCellValue(rowData.getRound2());
            cell = row.createCell(12);
            cell.setCellValue(rowData.getRound3());
            cell = row.createCell(13);
            cell.setCellValue(rowData.getRound4());
            cell = row.createCell(14);
            cell.setCellValue(rowData.getRound5());
            cell = row.createCell(15);
            cell.setCellValue(rowData.getRound6());
            cell = row.createCell(16);
            cell.setCellValue(rowData.getRound7());
            cell = row.createCell(17);
            cell.setCellValue(rowData.getRound8());
            cell = row.createCell(18);
            cell.setCellValue(rowData.getType());
        } catch (Exception e) {
            System.out.println(row.toString());
        }
    }

    private static void addTeamData(Row row, int startColumn, String team, Integer total, CellStyle mainTextStyle) {
        Cell cell = row.createCell(startColumn);
        cell.setCellValue(team);
        cell.setCellStyle(mainTextStyle);
        cell = row.createCell(startColumn + 1);
        cell.setCellValue(total);
        cell.setCellStyle(mainTextStyle);
    }

    private static void addPlayerData(Row row, int column, String athlete, Integer total, String team, CellStyle mainTextStyle) {
        if (row != null) {
            Cell cell = row.createCell(column);
            cell.setCellValue(athlete);
            cell.setCellStyle(mainTextStyle);
            cell = row.createCell(column + 1);
            cell.setCellValue(total);
            cell.setCellStyle(mainTextStyle);
            cell = row.createCell(column + 2);
            cell.setCellValue(team);
            cell.setCellStyle(mainTextStyle);
        }
    }

    private List<TeamScore> getTeamScores(List<Map.Entry<String, ArrayList<IndividualTotal>>> teamData) {
        HashMap<String, TeamScore> teamScoresThatCount = new HashMap<>();
        for (Map.Entry<String, ArrayList<IndividualTotal>> total : teamData) {
            var details = new TeamScore(total.getValue().get(0).getTeam(), 0);
            teamScoresThatCount.put(total.getValue().get(0).getTeam(), details);
        }

        for (Map.Entry<String, ArrayList<IndividualTotal>> total : teamData) {
            TeamScore teamTotal = teamScoresThatCount.get(total.getValue().get(0).getTeam());
            for (IndividualTotal indTotal : total.getValue()) {
                int currentTotal = teamTotal.getTotal();
                teamTotal.setTotal(currentTotal + indTotal.getTotal());
                teamScoresThatCount.put(indTotal.getTeam(), teamTotal);
            }
        }

        return teamScoresThatCount.values().stream().sorted(Comparator.comparingInt(TeamScore::getTotal).reversed()).toList();
    }

    private void populateTeamData(Sheet sheet, String teamType, CellStyle mainTextStyle, HashMap<String, ArrayList<IndividualTotal>> teamScoresByTotal) {
        excelHelper.setCurrentDateHeader(sheet, currentDate);
        excelHelper.setCurrentSeasonHeader(sheet);

        int rows = sheet.getLastRowNum();
        Row row;

        int updateRow = rows;
        int startColumn = 1;
        long start = System.currentTimeMillis();

        List<Map.Entry<String, ArrayList<IndividualTotal>>> teamData = teamScoresByTotal.entrySet().stream().filter(f -> f.getValue().get(0).getTeamClassification().equals(teamType) && f.getValue().get(0).getType().equals("singles")).toList();
        List<TeamScore> teamScores = getTeamScores(teamData);

        start = System.currentTimeMillis();
        System.out.println("Ran query for singles by " + teamType + " " + (System.currentTimeMillis() - start) + "ms");
        for (TeamScore teamScore : teamScores) {
            row = sheet.createRow(++updateRow);
            addTeamData(row, startColumn, teamScore.getName(), teamScore.getTotal(), mainTextStyle);
        }

        if (!"Rookie".equals(teamType)) {
            startColumn += 3;

            updateRow = rows;
            start = System.currentTimeMillis();
            teamData = teamScoresByTotal.entrySet().stream().filter(f -> f.getValue().get(0).getTeamClassification().equals(teamType) && f.getValue().get(0).getType().equals("handicap")).toList();
            teamScores = getTeamScores(teamData);
            System.out.println("Ran query for handicap by " + teamType + " " + (System.currentTimeMillis() - start) + "ms");
            for (TeamScore teamScore : teamScores) {
                row = sheet.getRow(++updateRow);
                addTeamData(row, startColumn, teamScore.getName(), teamScore.getTotal(), mainTextStyle);
            }
            startColumn += 3;

            updateRow = rows;
            start = System.currentTimeMillis();
            teamData = teamScoresByTotal.entrySet().stream().filter(f -> f.getValue().get(0).getTeamClassification().equals(teamType) && f.getValue().get(0).getType().equals("doubles")).toList();
            teamScores = getTeamScores(teamData);
            System.out.println("Ran query for doubles by " + teamType + " " + (System.currentTimeMillis() - start) + "ms");
            for (TeamScore teamScore : teamScores) {
                row = sheet.getRow(++updateRow);
                addTeamData(row, startColumn, teamScore.getName(), teamScore.getTotal(), mainTextStyle);
            }
            startColumn += 3;

            updateRow = rows;
            start = System.currentTimeMillis();
            teamData = teamScoresByTotal.entrySet().stream().filter(f -> f.getValue().get(0).getTeamClassification().equals(teamType) && f.getValue().get(0).getType().equals("skeet")).toList();
            teamScores = getTeamScores(teamData);
            System.out.println("Ran query for skeet by " + teamType + " " + (System.currentTimeMillis() - start) + "ms");
            for (TeamScore teamScore : teamScores) {
                row = sheet.getRow(++updateRow);
                addTeamData(row, startColumn, teamScore.getName(), teamScore.getTotal(), mainTextStyle);
            }
            startColumn += 3;

            updateRow = rows;
            start = System.currentTimeMillis();
            teamData = teamScoresByTotal.entrySet().stream().filter(f -> f.getValue().get(0).getTeamClassification().equals(teamType) && f.getValue().get(0).getType().equals("clays")).toList();
            teamScores = getTeamScores(teamData);
            System.out.println("Ran query for clays by " + teamType + " " + (System.currentTimeMillis() - start) + "ms");
            for (TeamScore teamScore : teamScores) {
                row = sheet.getRow(++updateRow);
                addTeamData(row, startColumn, teamScore.getName(), teamScore.getTotal(), mainTextStyle);
            }
            startColumn += 3;

            updateRow = rows;
            start = System.currentTimeMillis();
            teamData = teamScoresByTotal.entrySet().stream().filter(f -> f.getValue().get(0).getTeamClassification().equals(teamType) && f.getValue().get(0).getType().equals("fivestand")).toList();
            teamScores = getTeamScores(teamData);
            System.out.println("Ran query for fivestand by " + teamType + " " + (System.currentTimeMillis() - start) + "ms");
            for (TeamScore teamScore : teamScores) {
                row = sheet.getRow(++updateRow);
                addTeamData(row, startColumn, teamScore.getName(), teamScore.getTotal(), mainTextStyle);
            }
            startColumn += 3;

            updateRow = rows;
            start = System.currentTimeMillis();
            teamData = teamScoresByTotal.entrySet().stream().filter(f -> f.getValue().get(0).getTeamClassification().equals(teamType) && f.getValue().get(0).getType().equals("doublesskeet")).toList();
            teamScores = getTeamScores(teamData);
            System.out.println("Ran query for doublesskeet by " + teamType + " " + (System.currentTimeMillis() - start) + "ms");
            for (TeamScore teamScore : teamScores) {
                row = sheet.getRow(++updateRow);
                addTeamData(row, startColumn, teamScore.getName(), teamScore.getTotal(), mainTextStyle);
            }
        }
    }

    private void populateIndividualData(Workbook workbook, String sheetName, String gender, CellStyle style, CellStyle mainTextStyle, HashMap<String, IndividualTotal> allRoundScores) {
        long initialStart = System.currentTimeMillis();
        Sheet sheet = workbook.getSheet(sheetName);
        excelHelper.setCurrentDateHeader(sheet, currentDate);
        excelHelper.setCurrentSeasonHeader(sheet);

        int rows = sheet.getLastRowNum();
        Cell cell;
        Row row;

        int updateRow;
        int maxRow = rows;
        int classificationStartRow;
        boolean addBlankRowForHeader = false;
        List<String> classificationList = Arrays.asList("Varsity", "Junior Varsity", "Intermediate Advanced", "Intermediate Entry", "Rookie");
        long start;
        for (String classification : classificationList) {
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

//            var individualSinglesData = allRoundScores.entrySet().stream().filter(a -> a.getValue().getGender().equals(gender) && a.getValue().getClassification().equals(classification) && a.getValue().getType().equals("singles")).toList();
            List<IndividualTotal> justValues = new ArrayList<>(allRoundScores.values());
            justValues.sort(Comparator.comparingInt(IndividualTotal::getTotal).reversed());

            start = System.currentTimeMillis();
            var individualSinglesData = justValues.stream().filter(f -> f.getGender().equals(gender) && f.getClassification().equals(classification) && f.getType().equals("singles")).toList();
            System.out.println("Ran query for singles by " + gender + " and " + classification + " " + (System.currentTimeMillis() - start) + "ms");

            for (IndividualTotal singlesRowData : individualSinglesData) {
                row = sheet.createRow(++updateRow);
                addPlayerData(row, column, singlesRowData.getAthlete(), singlesRowData.getTotal(), singlesRowData.getTeam(), mainTextStyle);
            }
            column += 4;
            maxRow = Math.max(maxRow, updateRow);

            updateRow = classificationStartRow;
            updateRow++;
            start = System.currentTimeMillis();
            var individualHandicapData = justValues.stream().filter(f -> f.getGender().equals(gender) && f.getClassification().equals(classification) && f.getType().equals("handicap")).toList();
            System.out.println("Ran query for handicap by " + gender + " and " + classification + " " + (System.currentTimeMillis() - start) + "ms");
            for (IndividualTotal handicapRowData : individualHandicapData) {
                row = sheet.getRow(++updateRow);
                addPlayerData(row, column, handicapRowData.getAthlete(), handicapRowData.getTotal(), handicapRowData.getTeam(), mainTextStyle);
            }
            maxRow = Math.max(maxRow, updateRow);
            column += 4;
//
            updateRow = classificationStartRow;
            updateRow++;
            start = System.currentTimeMillis();
            var individualDoublesData = justValues.stream().filter(f -> f.getGender().equals(gender) && f.getClassification().equals(classification) && f.getType().equals("doubles")).toList();
            System.out.println("Ran query for handicap by " + gender + " and " + classification + " " + (System.currentTimeMillis() - start) + "ms");
            for (IndividualTotal doublesRowData : individualDoublesData) {
                row = sheet.getRow(++updateRow);
                addPlayerData(row, column, doublesRowData.getAthlete(), doublesRowData.getTotal(), doublesRowData.getTeam(), mainTextStyle);
            }
            maxRow = Math.max(maxRow, updateRow);
            column += 4;

            updateRow = classificationStartRow;
            updateRow++;
            start = System.currentTimeMillis();
            var individualSkeetData = justValues.stream().filter(f -> f.getGender().equals(gender) && f.getClassification().equals(classification) && f.getType().equals("skeet")).toList();
            System.out.println("Ran query for skeet by " + gender + " and " + classification + " " + (System.currentTimeMillis() - start) + "ms");
            for (IndividualTotal skeetRowData : individualSkeetData) {
                row = sheet.getRow(++updateRow);
                addPlayerData(row, column, skeetRowData.getAthlete(), skeetRowData.getTotal(), skeetRowData.getTeam(), mainTextStyle);
            }
            maxRow = Math.max(maxRow, updateRow);
            column += 4;

            updateRow = classificationStartRow;
            updateRow++;
            start = System.currentTimeMillis();
            var individualClaysData = justValues.stream().filter(f -> f.getGender().equals(gender) && f.getClassification().equals(classification) && f.getType().equals("clays")).toList();
            System.out.println("Ran query for clays by " + gender + " and " + classification + " " + (System.currentTimeMillis() - start) + "ms");
            for (IndividualTotal claysRowData : individualClaysData) {
                row = sheet.getRow(++updateRow);
                addPlayerData(row, column, claysRowData.getAthlete(), claysRowData.getTotal(), claysRowData.getTeam(), mainTextStyle);
            }
            maxRow = Math.max(maxRow, updateRow);
            column += 4;

            updateRow = classificationStartRow;
            updateRow++;
            start = System.currentTimeMillis();
            var individualFivestandData = justValues.stream().filter(f -> f.getGender().equals(gender) && f.getClassification().equals(classification) && f.getType().equals("fivestand")).toList();
            System.out.println("Ran query for fivestand by " + gender + " and " + classification + " " + (System.currentTimeMillis() - start) + "ms");
            for (IndividualTotal fivestandRowData : individualFivestandData) {
                row = sheet.getRow(++updateRow);
                addPlayerData(row, column, fivestandRowData.getAthlete(), fivestandRowData.getTotal(), fivestandRowData.getTeam(), mainTextStyle);
            }
            maxRow = Math.max(maxRow, updateRow);
            column += 4;

            updateRow = classificationStartRow;
            updateRow++;
            start = System.currentTimeMillis();
            var individualDoubleSkeetData = justValues.stream().filter(f -> f.getGender().equals(gender) && f.getClassification().equals(classification) && f.getType().equals("doublesskeet")).toList();
            System.out.println("Ran query for doublesskeet by " + gender + " and " + classification + " " + (System.currentTimeMillis() - start) + "ms");
            for (IndividualTotal doublesskeetRowData : individualDoubleSkeetData) {
                row = sheet.getRow(++updateRow);
                addPlayerData(row, column, doublesskeetRowData.getAthlete(), doublesskeetRowData.getTotal(), doublesskeetRowData.getTeam(), mainTextStyle);
            }
            maxRow = Math.max(maxRow, updateRow);

            sheet.setAutoFilter(CellRangeAddress.valueOf("A13:AB13"));
        }
        System.out.println(sheetName + " data populated in " + (System.currentTimeMillis() - initialStart) + "ms");
    }

    private HashMap<String, ArrayList<IndividualTotal>> calculateTeamScores(List<IndividualTotal> justValues) {
        HashMap<String, ArrayList<IndividualTotal>> teamScoresThatCount = new HashMap<>();
        for (IndividualTotal total : justValues) {
            teamScoresThatCount.put(total.getTeamForScores(), new ArrayList<>());
        }

        for (IndividualTotal total : justValues) {
            var currentTeam = teamScoresThatCount.get(total.getTeamForScores());
            var scoresToCount = total.getType().equals("singles") || total.getType().equals("handicap") || total.getType().equals("doubles") ? 5 : 3;
            if (currentTeam.size() < scoresToCount) {
                total.setClassification(total.getTeamClassification());
                currentTeam.add(total);
                teamScoresThatCount.put(total.getTeamForScores(), currentTeam);
            }
        }

        return teamScoresThatCount;
    }

    private List<IndividualTotal> getTeamScoresByTotal(HashMap<String, IndividualTotal> allRoundScores) {
        List<IndividualTotal> teamScoresByTotal = new ArrayList<>(allRoundScores.values());
        teamScoresByTotal.sort(Comparator.comparingInt(IndividualTotal::getTotal).reversed());
        return teamScoresByTotal;
    }

    private void populateTeamIndividualData(Workbook workbook, String sheetName, List<IndividualTotal> teamScoresByTotal) {
        Sheet sheet = workbook.getSheet(sheetName);
        long startTime = System.currentTimeMillis();
        long start = System.currentTimeMillis();
        System.out.println("Ran query for team scores " + (System.currentTimeMillis() - start) + "ms");

        int rows = sheet.getLastRowNum();

        var teamScoresThatCount = calculateTeamScores(teamScoresByTotal);
        for (List<IndividualTotal> individualTotalList : teamScoresThatCount.values()) {
            for (IndividualTotal rowData : individualTotalList) {
                rows = getRows(sheet, rows, rowData);
            }
        }

        sheet.setAutoFilter(CellRangeAddress.valueOf("A1:E1"));
        System.out.println("Team Individual Scores data populated in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private static int getRows(Sheet sheet, int rows, IndividualTotal rowData) {
        Row row = sheet.createRow(++rows);
        Cell cell = row.createCell(0);
        cell.setCellValue(rowData.getType());
        cell = row.createCell(1);
        cell.setCellValue(rowData.getTeam());
        cell = row.createCell(2);
        cell.setCellValue(rowData.getClassification());
        cell = row.createCell(3);
        cell.setCellValue(rowData.getAthlete());
        cell = row.createCell(4);
        cell.setCellValue(rowData.getTotal());
        return rows;
    }

    private void populateAllIndividualData(Workbook workbook, String sheetName, HashMap<String, IndividualTotal> allRoundScores) {
        Sheet sheet = workbook.getSheet(sheetName);
        long trueStart = System.currentTimeMillis();
        long start = System.currentTimeMillis();

        List<IndividualTotal> justValues = new ArrayList<>(allRoundScores.values());
        justValues.sort(Comparator.comparing(IndividualTotal::getTeam));
        System.out.println("Ran query for all scores " + (System.currentTimeMillis() - start) + "ms");

        int rows = sheet.getLastRowNum();

        Cell cell;
        Row row;
        for (IndividualTotal rowData : justValues) {
            row = sheet.createRow(++rows);
            cell = row.createCell(0);
            cell.setCellValue(rowData.getType());
            cell = row.createCell(1);
            cell.setCellValue(rowData.getTeam());
            cell = row.createCell(2);
            cell.setCellValue(rowData.getClassification());
            cell = row.createCell(3);
            cell.setCellValue(rowData.getAthlete());
            cell = row.createCell(4);
            cell.setCellValue(rowData.getTotal());
            cell = row.createCell(5);
            cell.setCellValue(rowData.getGender());
        }
        sheet.setAutoFilter(CellRangeAddress.valueOf("A1:F1"));
        System.out.println("Individual All Scores data populated in " + (System.currentTimeMillis() - trueStart) + "ms");
    }

}
