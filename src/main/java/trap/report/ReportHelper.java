package trap.report;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import trap.model.AllData;
import trap.model.AllIndividualScores;
import trap.model.AllTeamScores;
import trap.model.ClaysAggregate;
import trap.model.ClaysTeamAggregate;
import trap.model.DoublesAggregate;
import trap.model.DoublesTeamAggregate;
import trap.model.FiveStandAggregate;
import trap.model.FiveStandAllData;
import trap.model.FiveStandAllIndividualScores;
import trap.model.FiveStandAllTeamScores;
import trap.model.FiveStandTeamAggregate;
import trap.model.HandicapAggregate;
import trap.model.HandicapTeamAggregate;
import trap.model.SinglesAggregate;
import trap.model.SinglesTeamAggregate;
import trap.model.SkeetAggregate;
import trap.model.SkeetTeamAggregate;
import trap.repository.AllDataRepository;
import trap.repository.AllIndividualScoresRepository;
import trap.repository.AllTeamScoresRepository;
import trap.repository.ClaysDataRepository;
import trap.repository.ClaysDataTeamRepository;
import trap.repository.DoublesDataRepository;
import trap.repository.DoublesDataTeamRepository;
import trap.repository.FiveStandAllDataRepository;
import trap.repository.FiveStandAllIndividualScoresRepository;
import trap.repository.FiveStandAllTeamScoresRepository;
import trap.repository.FiveStandDataRepository;
import trap.repository.FiveStandDataTeamRepository;
import trap.repository.HandicapDataRepository;
import trap.repository.HandicapDataTeamRepository;
import trap.repository.SinglesDataRepository;
import trap.repository.SinglesDataTeamRepository;
import trap.repository.SkeetDataRepository;
import trap.repository.SkeetDataTeamRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ReportHelper {
    private final JdbcTemplate jdbc;
    private final SinglesDataRepository singlesRepository;
    private final SinglesDataTeamRepository singlesDataTeamRepository;
    private final DoublesDataRepository doublesDataRepository;
    private final DoublesDataTeamRepository doublesDataTeamRepository;
    private final HandicapDataRepository handicapDataRepository;
    private final HandicapDataTeamRepository handicapDataTeamRepository;
    private final SkeetDataRepository skeetDataRepository;
    private final SkeetDataTeamRepository skeetDataTeamRepository;
    private final ClaysDataRepository claysDataRepository;
    private final ClaysDataTeamRepository claysDataTeamRepository;
    private final AllDataRepository allDataRepository;
    private final AllTeamScoresRepository allTeamScoresRepository;
    private final AllIndividualScoresRepository allIndividualScoresRepository;
    private final FiveStandDataRepository fiveStandDataRepository;
    private final FiveStandDataTeamRepository fiveStandDataTeamRepository;
    private final FiveStandAllDataRepository fiveStandAllDataRepository;
    private final FiveStandAllTeamScoresRepository fiveStandAllTeamScoresRepository;
    private final FiveStandAllIndividualScoresRepository fiveStandAllIndividualScoresRepository;
    private final String currentDate = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
    private final String[] trapTypes = new String[]{"singles", "doubles", "handicap", "skeet", "clays", "fivestand"};

    public void doItAll() throws Exception {
        downloadFiles();
        addFilesToDatabase();
        fixTeamNames();

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

        CellStyle mainTextStyle = getCellStyle(workbook);
        CellStyle style = setFontForHeaders(workbook);

        populateCleanData(workbook.getSheet("Clean Data"));

        for (Map.Entry<String, String> entry : types.entrySet()) {
            start = System.currentTimeMillis();
            populateTeamData(workbook.getSheet(entry.getKey()), entry.getValue(), mainTextStyle);
            System.out.println("" + entry.getKey() + " data populated in " + (System.currentTimeMillis() - start) + "ms");
        }

        populateIndividualData(workbook, "Individual-Men", "M", style, mainTextStyle);
        populateIndividualData(workbook, "Individual-Ladies", "F", style, mainTextStyle);

        populateTeamIndividualData(workbook, "Team-Individual-Scores");
        populateAllIndividualData(workbook, "Individual-All-Scores");

//        populateFiveStandCleanData(workbook.getSheet("Clean Data"));

//        for (Map.Entry<String, String> entry : types.entrySet()) {
//            start = System.currentTimeMillis();
//            populateFiveStandTeamData(workbook.getSheet(entry.getKey()), entry.getValue(), mainTextStyle);
//            System.out.println("" + entry.getKey() + " data populated in " + (System.currentTimeMillis() - start) + "ms");
//        }
//
//        populateFiveStandIndividualData(workbook, "Individual-Men", "M", style, mainTextStyle);
//        populateFiveStandIndividualData(workbook, "Individual-Ladies", "F", style, mainTextStyle);
//
//        populateFiveStandTeamIndividualData(workbook, "Team-Individual-Scores");
//        populateFiveStandAllIndividualData(workbook, "Individual-All-Scores");

        createFile(workbook, "league-data");

        System.out.println("Finished creating file in " + (System.currentTimeMillis() - trueStart) + "ms");
        workbook.close();
    }

    private void downloadFiles() throws IOException {
        long start = System.currentTimeMillis();
        System.out.println("Started downloading files");

        Map<String, String> fileUrls = new HashMap<>();
        fileUrls.put("singles", "https://metabase.sssfonline.com/public/question/8648faf9-42e8-4a9c-b55d-2f251349de7f.csv");
        fileUrls.put("doubles", "https://metabase.sssfonline.com/public/question/5d5a78a5-2356-477f-b1b8-fe6ee11d25b1.csv");
        fileUrls.put("handicap", "https://metabase.sssfonline.com/public/question/69ca55d9-3e18-45bc-b57f-73aeb205ece8.csv");
        fileUrls.put("skeet", "https://metabase.sssfonline.com/public/question/c697d744-0e06-4c3f-a640-fea02f9c9ecd.csv");
        fileUrls.put("clays", "https://metabase.sssfonline.com/public/question/2c6edb1a-a7ee-43c2-8180-ad199a57be55.csv");
        fileUrls.put("fivestand", "https://metabase.sssfonline.com/public/question/3c5aecf2-a9f2-49b2-a11f-36965cb1a964.csv");

        Charset charset = StandardCharsets.UTF_8;
        for (String type : trapTypes) {
            System.out.println("Downloading " + type + " file");
//            FileUtils.copyURLToFile(new URL(fileUrls.get(type)), new File(type + ".csv"), 60000, 60000);
            System.out.println("Finished downloading " + type + " file");

            System.out.println("Replacing double spaces for " + type + " file");
            Path path = Paths.get(type + ".csv");
            String content = Files.readString(path, charset);
            content = content.replaceAll(" {2}", " ");
            Files.writeString(path, content, charset);
            System.out.println("Finished replacing double spaces for " + type + " file");
        }
        System.out.println("Files downloaded in " + (System.currentTimeMillis() - start) + " ms");
    }

    private void addFilesToDatabase() throws Exception {
        long start = System.currentTimeMillis();
        jdbc.execute("set global local_infile=1;");
        String singlesSql = "load data local infile 'singles.csv' into table singles fields terminated by ',' OPTIONALLY ENCLOSED BY '\"' lines terminated by '\n' IGNORE 1 LINES;";
        int singlesCount = jdbc.update(con -> con.prepareStatement(singlesSql));
        System.out.println("Added " + singlesCount + " new records to database in singles table.");

        String doublesSql = "load data local infile 'doubles.csv' into table doubles fields terminated by ',' OPTIONALLY ENCLOSED BY '\"' lines terminated by '\n' IGNORE 1 LINES;";
        int doublesCount = jdbc.update(con -> con.prepareStatement(doublesSql));
        System.out.println("Added " + doublesCount + " new records to database in doubles table.");

        String handicapSql = "load data local infile 'handicap.csv' into table handicap fields terminated by ',' OPTIONALLY ENCLOSED BY '\"' lines terminated by '\n' IGNORE 1 LINES;";
        int handicapCount = jdbc.update(con -> con.prepareStatement(handicapSql));
        System.out.println("Added " + handicapCount + " new records to database in handicap table.");

        String skeetSql = "load data local infile 'skeet.csv' into table skeet fields terminated by ',' OPTIONALLY ENCLOSED BY '\"' lines terminated by '\n' IGNORE 1 LINES;";
        int skeetCount = jdbc.update(con -> con.prepareStatement(skeetSql));
        System.out.println("Added " + skeetCount + " new records to database in skeet table.");

        String claysSql = "load data local infile 'clays.csv' into table clays fields terminated by ',' OPTIONALLY ENCLOSED BY '\"' lines terminated by '\n' IGNORE 1 LINES;";
        int claysCount = jdbc.update(con -> con.prepareStatement(claysSql));
        System.out.println("Added " + claysCount + " new records to database in clays table.");

        int rowsAdded = singlesCount + doublesCount + handicapCount + skeetCount + claysCount;
        System.out.println("Added " + rowsAdded + " total records to database");
        List<AllData> allData = allDataRepository.findAll();
        System.out.println("Found " + allData.size() + " total records in database");

        if (rowsAdded != allData.size()) {
            throw new Exception("rows not added to database properly");
        }

        String fivestandSql = "load data local infile 'fivestand.csv' into table fivestand fields terminated by ',' OPTIONALLY ENCLOSED BY '\"' lines terminated by '\n' IGNORE 1 LINES;";
        int fivestandCount = jdbc.update(con -> con.prepareStatement(fivestandSql));
        System.out.println("Added " + fivestandCount + " new records to database in fivestand table.");

        List<FiveStandAllData> fiveStandAllData = fiveStandAllDataRepository.findAll();
        System.out.println("Found " + fiveStandAllData.size() + " records in fivestand table.");
        System.out.println("Database loaded in " + (System.currentTimeMillis() - start) + "ms");
    }
    private void fixTeamNames() {
        long start = System.currentTimeMillis();
        jdbc.execute("UPDATE singles SET team = replace(team, 'Club', 'Team') WHERE team LIKE '%Club%';");
        jdbc.execute("UPDATE doubles SET team = replace(team, 'Club', 'Team') WHERE team LIKE '%Club%';");
        jdbc.execute("UPDATE handicap SET team = replace(team, 'Club', 'Team') WHERE team LIKE '%Club%';");
        jdbc.execute("UPDATE skeet SET team = replace(team, 'Club', 'Team') WHERE team LIKE '%Club%';");
        jdbc.execute("UPDATE clays SET team = replace(team, 'Club', 'Team') WHERE team LIKE '%Club%';");
        jdbc.execute("UPDATE fivestand SET team = replace(team, 'Club', 'Team') WHERE team LIKE '%Club%';");
        System.out.println("Team names fixed in " + (System.currentTimeMillis() - start) + "ms");
    }

    private Workbook getWorkbook(String templateName) throws IOException {
        InputStream in = getClass().getResourceAsStream("/" + templateName + "-template.xlsx");
        return WorkbookFactory.create(Objects.requireNonNull(in));
    }

    private void populateCleanData(Sheet sheet) {
        long start = System.currentTimeMillis();
        List<AllData> allData = allDataRepository.findAll();
        //List<FiveStand> allData = fiveStandDataRepository.findAll();
        System.out.println("Ran get all data for clean data population " + (System.currentTimeMillis() - start) + "ms");

        int rows = sheet.getLastRowNum();

        Cell cell;
        Row row;
        for (AllData rowData : allData) {
            row = sheet.createRow(++rows);
            cell = row.createCell(0);
            cell.setCellValue(rowData.getEventid());
            cell = row.createCell(1);
            cell.setCellValue(rowData.getEvent());
            cell = row.createCell(2);
            cell.setCellValue(rowData.getLocationid());
            cell = row.createCell(3);
            cell.setCellValue(rowData.getLocation());
            cell = row.createCell(4);
            cell.setCellValue(rowData.getEventdate());
            cell = row.createCell(5);
            cell.setCellValue(rowData.getSquadname());
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
        }
        sheet.setAutoFilter(CellRangeAddress.valueOf("A1:S1"));
        System.out.println("Clean data populated in " + (System.currentTimeMillis() - start) + "ms");
    }

    private CellStyle setFontForHeaders(Workbook workbook) {
        Font font = workbook.createFont();
        font.setFontName("Calibri");
        font.setItalic(true);
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    private CellStyle getCellStyle(Workbook workbook) {
        Font mainText = workbook.createFont();
        mainText.setFontName("Calibri");
        mainText.setFontHeightInPoints((short) 12);
        CellStyle mainTextStyle = workbook.createCellStyle();
        mainTextStyle.setFont(mainText);
        return mainTextStyle;
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

    private void populateTeamData(Sheet sheet, String teamType, CellStyle mainTextStyle) {
        setCurrentDateHeader(sheet);
        setCurrentSeasonHeader(sheet);

        int rows = sheet.getLastRowNum();
        Row row;

        int updateRow = rows;
        int startColumn = 1;
        long start = System.currentTimeMillis();
        List<SinglesTeamAggregate> singlesTeamData = singlesDataTeamRepository.getAllByClassificationOrderByTotalDesc(teamType);
        System.out.println("Ran query for singles by " + teamType + " " + (System.currentTimeMillis() - start) + "ms");
        for (SinglesTeamAggregate singlesTeamRowData : singlesTeamData) {
            row = sheet.createRow(++updateRow);
            addTeamData(row, startColumn, singlesTeamRowData.getTeam(), singlesTeamRowData.getTotal(), mainTextStyle);
        }

        if (!"Rookie".equals(teamType)) {
            startColumn += 3;

            updateRow = rows;
            start = System.currentTimeMillis();
            List<HandicapTeamAggregate> handicapTeamData = handicapDataTeamRepository.getAllByClassificationOrderByTotalDesc(teamType);
            System.out.println("Ran query for handicap by " + teamType + " " + (System.currentTimeMillis() - start) + "ms");
            for (HandicapTeamAggregate handicapTeamRowData : handicapTeamData) {
                row = sheet.getRow(++updateRow);
                addTeamData(row, startColumn, handicapTeamRowData.getTeam(), handicapTeamRowData.getTotal(), mainTextStyle);
            }
            startColumn += 3;

            updateRow = rows;
            start = System.currentTimeMillis();
            List<DoublesTeamAggregate> doublesTeamData = doublesDataTeamRepository.getAllByClassificationOrderByTotalDesc(teamType);
            System.out.println("Ran query for doubles by " + teamType + " " + (System.currentTimeMillis() - start) + "ms");
            for (DoublesTeamAggregate doublesTeamRowData : doublesTeamData) {
                row = sheet.getRow(++updateRow);
                addTeamData(row, startColumn, doublesTeamRowData.getTeam(), doublesTeamRowData.getTotal(), mainTextStyle);
            }
            startColumn += 3;

            updateRow = rows;
            start = System.currentTimeMillis();
            List<SkeetTeamAggregate> skeetTeamData = skeetDataTeamRepository.getAllByClassificationOrderByTotalDesc(teamType);
            System.out.println("Ran query for skeet by " + teamType + " " + (System.currentTimeMillis() - start) + "ms");
            for (SkeetTeamAggregate skeetTeamRowData : skeetTeamData) {
                row = sheet.getRow(++updateRow);
                addTeamData(row, startColumn, skeetTeamRowData.getTeam(), skeetTeamRowData.getTotal(), mainTextStyle);
            }
            startColumn += 3;

            updateRow = rows;
            start = System.currentTimeMillis();
            List<ClaysTeamAggregate> claysTeamData = claysDataTeamRepository.getAllByClassificationOrderByTotalDesc(teamType);
            System.out.println("Ran query for clays by " + teamType + " " + (System.currentTimeMillis() - start) + "ms");
            for (ClaysTeamAggregate claysTeamRowData : claysTeamData) {
                row = sheet.getRow(++updateRow);
                addTeamData(row, startColumn, claysTeamRowData.getTeam(), claysTeamRowData.getTotal(), mainTextStyle);
            }
            startColumn += 3;

            updateRow = rows;
            start = System.currentTimeMillis();
            List<FiveStandTeamAggregate> fivestandTeamData = fiveStandDataTeamRepository.getAllByClassificationOrderByTotalDesc(teamType);
            System.out.println("Ran query for fivestand by " + teamType + " " + (System.currentTimeMillis() - start) + "ms");
            for (FiveStandTeamAggregate fiveStandTeamRowData : fivestandTeamData) {
                row = sheet.getRow(++updateRow);
                addTeamData(row, startColumn, fiveStandTeamRowData.getTeam(), fiveStandTeamRowData.getTotal(), mainTextStyle);
            }
        }
    }

    private void populateIndividualData(Workbook workbook, String sheetName, String gender, CellStyle style, CellStyle mainTextStyle) {
        long initialStart = System.currentTimeMillis();
        Sheet sheet = workbook.getSheet(sheetName);
        setCurrentDateHeader(sheet);
        setCurrentSeasonHeader(sheet);

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
            List<SinglesAggregate> individualSinglesData = singlesRepository.getAllByGenderAndClassification(gender, classification);
            System.out.println("Ran query for singles by " + gender + " and " + classification + " " + (System.currentTimeMillis() - start) + "ms");

            for (SinglesAggregate singlesRowData : individualSinglesData) {
                row = sheet.createRow(++updateRow);
                addPlayerData(row, column, singlesRowData.getAthlete(), singlesRowData.getTotal(), singlesRowData.getTeam(), mainTextStyle);
            }
            column += 4;
            maxRow = Math.max(maxRow, updateRow);

            updateRow = classificationStartRow;
            updateRow++;
            start = System.currentTimeMillis();
            List<HandicapAggregate> individualHandicapData = handicapDataRepository.getAllByGenderAndClassification(gender, classification);
            System.out.println("Ran query for handicap by " + gender + " and " + classification + " " + (System.currentTimeMillis() - start) + "ms");
            for (HandicapAggregate handicapRowData : individualHandicapData) {
                row = sheet.getRow(++updateRow);
                addPlayerData(row, column, handicapRowData.getAthlete(), handicapRowData.getTotal(), handicapRowData.getTeam(), mainTextStyle);
            }
            column += 4;

            updateRow = classificationStartRow;
            updateRow++;
            start = System.currentTimeMillis();
            List<DoublesAggregate> doublesIndividualData = doublesDataRepository.getAllByGenderAndClassification(gender, classification);
            System.out.println("Ran query for doubles by " + gender + " and " + classification + " " + (System.currentTimeMillis() - start) + "ms");
            for (DoublesAggregate doublesRowData : doublesIndividualData) {
                row = sheet.getRow(++updateRow);
                addPlayerData(row, column, doublesRowData.getAthlete(), doublesRowData.getTotal(), doublesRowData.getTeam(), mainTextStyle);
            }
            column += 4;

            updateRow = classificationStartRow;
            updateRow++;
            start = System.currentTimeMillis();
            List<SkeetAggregate> skeetIndividualData = skeetDataRepository.getAllByGenderAndClassification(gender, classification);
            System.out.println("Ran query for skeet by " + gender + " and " + classification + " " + (System.currentTimeMillis() - start) + "ms");
            for (SkeetAggregate skeetRowData : skeetIndividualData) {
                row = sheet.getRow(++updateRow);
                addPlayerData(row, column, skeetRowData.getAthlete(), skeetRowData.getTotal(), skeetRowData.getTeam(), mainTextStyle);
            }
            column += 4;

            updateRow = classificationStartRow;
            updateRow++;
            start = System.currentTimeMillis();
            List<ClaysAggregate> claysIndividualData = claysDataRepository.getAllByGenderAndClassification(gender, classification);
            System.out.println("Ran query for clays by " + gender + " and " + classification + " " + (System.currentTimeMillis() - start) + "ms");
            for (ClaysAggregate claysRowData : claysIndividualData) {
                row = sheet.getRow(++updateRow);
                addPlayerData(row, column, claysRowData.getAthlete(), claysRowData.getTotal(), claysRowData.getTeam(), mainTextStyle);
            }
            maxRow = Math.max(maxRow, updateRow);

            sheet.setAutoFilter(CellRangeAddress.valueOf("A13:T13"));
        }
        System.out.println(sheetName + " data populated in " + (System.currentTimeMillis() - initialStart) + "ms");
    }

    private void populateFiveStandIndividualData(Workbook workbook, String sheetName, String gender, CellStyle style, CellStyle mainTextStyle) {
        long initialStart = System.currentTimeMillis();
        Sheet sheet = workbook.getSheet(sheetName);
        setCurrentDateHeader(sheet);
        setCurrentSeasonHeader(sheet);

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

            start = System.currentTimeMillis();
            List<FiveStandAggregate> individualSinglesData = fiveStandDataRepository.getAllByGenderAndClassification(gender, classification);
            System.out.println("Ran query for singles by " + gender + " and " + classification + " " + (System.currentTimeMillis() - start) + "ms");

            for (FiveStandAggregate singlesRowData : individualSinglesData) {
                row = sheet.createRow(++updateRow);
                addPlayerData(row, column, singlesRowData.getAthlete(), singlesRowData.getTotal(), singlesRowData.getTeam(), mainTextStyle);
            }
            maxRow = Math.max(maxRow, updateRow);

            updateRow = classificationStartRow;
            updateRow++;

            sheet.setAutoFilter(CellRangeAddress.valueOf("B13:D13"));
        }
        System.out.println(sheetName + " data populated in " + (System.currentTimeMillis() - initialStart) + "ms");
    }

    private void setCurrentDateHeader(Sheet sheet) {
        sheet.getRow(9).getCell(1).setCellValue(sheet.getRow(9).getCell(1).getStringCellValue() + currentDate);
    }

    private void setCurrentSeasonHeader(Sheet sheet) {
        java.util.Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int currentSeason = month > 8 ? year + 1 : year;
        sheet.getRow(8).getCell(1).setCellValue(currentSeason + " " + sheet.getRow(8).getCell(1).getStringCellValue());
    }

    private void populateTeamIndividualData(Workbook workbook, String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        long startTime = System.currentTimeMillis();
        long start = System.currentTimeMillis();
        List<AllTeamScores> allData = allTeamScoresRepository.findAll();
        System.out.println("Ran query for team scores " + (System.currentTimeMillis() - start) + "ms");

        int rows = sheet.getLastRowNum();

        for (AllTeamScores rowData : allData) {
            rows = getRows(sheet, rows, rowData);
        }
        sheet.setAutoFilter(CellRangeAddress.valueOf("A1:E1"));
        System.out.println("Team Individual Scores data populated in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private static int getRows(Sheet sheet, int rows, AllTeamScores rowData) {
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
        cell.setCellValue(rowData.getIndtotal());
        return rows;
    }

    private void populateFiveStandTeamIndividualData(Workbook workbook, String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        long startTime = System.currentTimeMillis();
        long start = System.currentTimeMillis();
        List<FiveStandAllTeamScores> allData = fiveStandAllTeamScoresRepository.findAll();
        System.out.println("Ran query for team scores " + (System.currentTimeMillis() - start) + "ms");

        int rows = sheet.getLastRowNum();

        for (FiveStandAllTeamScores rowData : allData) {
            rows = getFiveStandRows(sheet, rows, rowData);
        }
        sheet.setAutoFilter(CellRangeAddress.valueOf("A1:E1"));
        System.out.println("Team Individual Scores data populated in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private static int getFiveStandRows(Sheet sheet, int rows, FiveStandAllTeamScores rowData) {
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
        cell.setCellValue(rowData.getIndtotal());
        return rows;
    }

    private void populateAllIndividualData(Workbook workbook, String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        long trueStart = System.currentTimeMillis();
        long start = System.currentTimeMillis();
        List<AllIndividualScores> allIndividualScores = allIndividualScoresRepository.findAllByOrderByTeamAscTypeAscClassificationAscGenderAscTotalDesc();
        System.out.println("Ran query for all scores " + (System.currentTimeMillis() - start) + "ms");

        int rows = sheet.getLastRowNum();

        Cell cell;
        Row row;
        for (AllIndividualScores rowData : allIndividualScores) {
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

    private void populateFiveStandAllIndividualData(Workbook workbook, String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        long trueStart = System.currentTimeMillis();
        long start = System.currentTimeMillis();
        List<FiveStandAllIndividualScores> allIndividualScores = fiveStandAllIndividualScoresRepository.findAllByOrderByTeamAscTypeAscClassificationAscGenderAscTotalDesc();
        System.out.println("Ran query for all scores " + (System.currentTimeMillis() - start) + "ms");

        int rows = sheet.getLastRowNum();

        Cell cell;
        Row row;
        for (FiveStandAllIndividualScores rowData : allIndividualScores) {
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

    private void createFile(Workbook workbook, String teamType) throws IOException {
        long start;
        start = System.currentTimeMillis();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDate = formatter.format(date);
        String newFilename = teamType + "-" + currentDate + ".xlsx";
        FileOutputStream fileOutputStream = new FileOutputStream(newFilename);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        System.out.println("Created file " + newFilename);
        System.out.println("Wrote the contents to a file in " + (System.currentTimeMillis() - start) + "ms");
    }
}
