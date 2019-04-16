package trap.report;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trap.model.AllData;
import trap.model.ClaysAggregate;
import trap.model.ClaysTeamAggregate;
import trap.model.DoublesAggregate;
import trap.model.DoublesTeamAggregate;
import trap.model.HandicapAggregate;
import trap.model.HandicapTeamAggregate;
import trap.model.SinglesAggregate;
import trap.model.SinglesTeamAggregate;
import trap.model.SkeetAggregate;
import trap.model.SkeetTeamAggregate;
import trap.repository.AllDataRepository;
import trap.repository.ClaysDataRepository;
import trap.repository.ClaysDataTeamRepository;
import trap.repository.DoublesDataRepository;
import trap.repository.DoublesDataTeamRepository;
import trap.repository.HandicapDataRepository;
import trap.repository.HandicapDataTeamRepository;
import trap.repository.SinglesDataRepository;
import trap.repository.SinglesDataTeamRepository;
import trap.repository.SkeetDataRepository;
import trap.repository.SkeetDataTeamRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

@RestController
@RequestMapping("/reports")
public class ReportController {

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

    private final List<String> classificationList = Arrays.asList("Varsity", "Junior Varsity", "Intermediate Advanced", "Intermediate Entry", "Rookie", "Collegiate");
    private final String currentDate = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());

    private static final Logger LOG = Logger.getLogger(ReportController.class.getName());

    private final JdbcTemplate jdbc;

    @Value("${trap.singles}")
    private String singles;
    @Value("${trap.doubles}")
    private String doubles;
    @Value("${trap.handicap}")
    private String handicap;
    @Value("${trap.skeet}")
    private String skeet;
    @Value("${trap.clays}")
    private String clays;

    @Autowired
    public ReportController(SinglesDataRepository singlesRepository, SinglesDataTeamRepository singlesDataTeamRepository, DoublesDataRepository doublesDataRepository, DoublesDataTeamRepository doublesDataTeamRepository, HandicapDataRepository handicapDataRepository, HandicapDataTeamRepository handicapDataTeamRepository, SkeetDataRepository skeetDataRepository, SkeetDataTeamRepository skeetDataTeamRepository, ClaysDataRepository claysDataRepository, ClaysDataTeamRepository claysDataTeamRepository, AllDataRepository allDataRepository, JdbcTemplate jdbcTemplate) {
        jdbc = jdbcTemplate;
        String result = checkFileImport();
        if (result.contains("OFF")) {
            ReportController.LOG.warning("local_infile is OFF.  File import is prohibited!");
        }

        this.singlesRepository = singlesRepository;
        this.singlesDataTeamRepository = singlesDataTeamRepository;
        this.doublesDataRepository = doublesDataRepository;
        this.doublesDataTeamRepository = doublesDataTeamRepository;
        this.handicapDataRepository = handicapDataRepository;
        this.handicapDataTeamRepository = handicapDataTeamRepository;
        this.skeetDataRepository = skeetDataRepository;
        this.skeetDataTeamRepository = skeetDataTeamRepository;
        this.claysDataRepository = claysDataRepository;
        this.claysDataTeamRepository = claysDataTeamRepository;
        this.allDataRepository = allDataRepository;
    }

    @RequestMapping("/checkFileImport")
    public String checkFileImport() {
        return jdbc.query("SHOW GLOBAL VARIABLES LIKE 'local_infile'", new Object[0], rs -> rs.next() ? rs.getString(1) + "=" + rs.getString(2) : "Count not determine value of local_infile");
    }

    private String saveDataToDatabase() {
        ReportController.LOG.fine("Saving trap data to database");

        jdbc.execute("TRUNCATE TABLE singles;");
        jdbc.execute("TRUNCATE TABLE doubles;");
        jdbc.execute("TRUNCATE TABLE handicap;");
        jdbc.execute("TRUNCATE TABLE skeet;");
        jdbc.execute("TRUNCATE TABLE clays;");

        StringBuilder results = new StringBuilder();
        String singlesSql = "load data local infile '" + singles + "' into table singles fields terminated by ',' OPTIONALLY ENCLOSED BY '\"' lines terminated by '\n' IGNORE 1 LINES;";
        int singlesCount = jdbc.update(con -> con.prepareStatement(singlesSql));
        results.append("Added ").append(singlesCount).append(" new records to database in singles table.<br>");

        String doublesSql = "load data local infile '" + doubles + "' into table doubles fields terminated by ',' OPTIONALLY ENCLOSED BY '\"' lines terminated by '\n' IGNORE 1 LINES;";
        int doublesCount = jdbc.update(con -> con.prepareStatement(doublesSql));
        results.append("Added ").append(doublesCount).append(" new records to database in doubles table.<br>");

        String handicapSql = "load data local infile '" + handicap + "' into table handicap fields terminated by ',' OPTIONALLY ENCLOSED BY '\"' lines terminated by '\n' IGNORE 1 LINES;";
        int handicapCount = jdbc.update(con -> con.prepareStatement(handicapSql));
        results.append("Added ").append(handicapCount).append(" new records to database in handicap table.<br>");

        String skeetSql = "load data local infile '" + skeet + "' into table skeet fields terminated by ',' OPTIONALLY ENCLOSED BY '\"' lines terminated by '\n' IGNORE 1 LINES;";
        int skeetCount = jdbc.update(con -> con.prepareStatement(skeetSql));
        results.append("Added ").append(skeetCount).append(" new records to database in skeet table.<br>");

        String claysSql = "load data local infile '" + clays + "' into table clays fields terminated by ',' OPTIONALLY ENCLOSED BY '\"' lines terminated by '\n' IGNORE 1 LINES;";
        int claysCount = jdbc.update(con -> con.prepareStatement(claysSql));
        results.append("Added ").append(claysCount).append(" new records to database in clays table.<br>");

        jdbc.execute("COMMIT;");

        fixNames();

        return results.toString();
    }

    private void fixNames() {
        jdbc.execute("UPDATE singles SET team = 'North Scott Trap Club' WHERE team = 'North Scott Trap Team';");
        jdbc.execute("UPDATE doubles SET team = 'North Scott Trap Club' WHERE team = 'North Scott Trap Team';");
        jdbc.execute("UPDATE handicap SET team = 'North Scott Trap Club' WHERE team = 'North Scott Trap Team';");
        jdbc.execute("UPDATE skeet SET team = 'North Scott Trap Club' WHERE team = 'North Scott Trap Team';");
        jdbc.execute("UPDATE clays SET team = 'North Scott Trap Club' WHERE team = 'North Scott Trap Team';");
        jdbc.execute("COMMIT;");
    }

    @RequestMapping("/export")
    public String export() throws IOException {
        StringBuilder result = new StringBuilder();

        result.append(saveDataToDatabase());

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("template.xls")).getFile());
        Workbook workbook = WorkbookFactory.create(file);

        result.append("Workbook has ").append(workbook.getNumberOfSheets()).append(" sheets");
        workbook.forEach(sheet -> result.append("<br>").append(sheet.getSheetName()));

        long start = System.currentTimeMillis();
        populateCleanData(workbook.getSheet("Clean Data"));
        result.append("<br>Clean data populated in ").append(System.currentTimeMillis() - start).append("ms");

        Map<String, String> types = new HashMap<>();
        types.put("Team-Senior", "Varsity");
        types.put("Team-Intermediate", "Intermediate Entry");
        types.put("Team-Rookie", "Rookie");
        types.put("Team-Collegiate", "Collegiate");

        //Set font for mainText
        Font mainText = workbook.createFont();
        mainText.setFontName("Calibri");
        mainText.setFontHeightInPoints((short) 12);
        CellStyle mainTextStyle = workbook.createCellStyle();
        mainTextStyle.setFont(mainText);

        for (Map.Entry<String, String> entry : types.entrySet()) {
            start = System.currentTimeMillis();
            populateTeamData(workbook.getSheet(entry.getKey()), entry.getValue(), mainTextStyle);
            result.append("<br>").append(entry.getKey()).append(" data populated in ").append(System.currentTimeMillis() - start).append("ms");
        }

        //Set font for headers
        Font font = workbook.createFont();
        font.setFontName("Calibri");
        font.setItalic(true);
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);

        start = System.currentTimeMillis();
        populateIndividualData(workbook.getSheet("Individual-Men"), "M", style, mainTextStyle);
        result.append("<br>Individual Men data populated in ").append(System.currentTimeMillis() - start).append("ms");

        start = System.currentTimeMillis();
        populateIndividualData(workbook.getSheet("Individual-Ladies"), "F", style, mainTextStyle);
        result.append("<br>Individual Women data populated in ").append(System.currentTimeMillis() - start).append("ms");

        start = System.currentTimeMillis();
        autoSizeColumns(workbook);
        result.append("<br>Auto sized all columns in ").append(System.currentTimeMillis() - start).append("ms");

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDate = formatter.format(date);

        FileOutputStream fileOutputStream = new FileOutputStream(currentDate + ".xls");
        workbook.write(fileOutputStream);
        fileOutputStream.close();

        workbook.close();

        return result.toString();
    }

    private void populateCleanData(Sheet sheet) {
        List<AllData> allData = allDataRepository.findAll();

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
            cell.setCellValue(rowData.getStation());
            cell = row.createCell(7);
            cell.setCellValue(rowData.getTeam());
            cell = row.createCell(8);
            cell.setCellValue(rowData.getAthlete());
            cell = row.createCell(9);
            cell.setCellValue(rowData.getClassification());
            cell = row.createCell(10);
            cell.setCellValue(rowData.getGender());
            cell = row.createCell(11);
            cell.setCellValue(rowData.getRound1());
            cell = row.createCell(12);
            cell.setCellValue(rowData.getRound2());
            cell = row.createCell(13);
            cell.setCellValue(rowData.getRound3());
            cell = row.createCell(14);
            cell.setCellValue(rowData.getRound4());
            cell = row.createCell(15);
            cell.setCellValue(rowData.getRound5());
            cell = row.createCell(16);
            cell.setCellValue(rowData.getRound6());
            cell = row.createCell(17);
            cell.setCellValue(rowData.getRound7());
            cell = row.createCell(18);
            cell.setCellValue(rowData.getRound8());
            cell = row.createCell(19);
            cell.setCellValue(rowData.getFrontrun());
            cell = row.createCell(20);
            cell.setCellValue(rowData.getBackrun());
            cell = row.createCell(21);
            cell.setCellValue(rowData.getRegisterdate());
            cell = row.createCell(22);
            cell.setCellValue(rowData.getRegisteredby());
            cell = row.createCell(23);
            cell.setCellValue(rowData.getFivestand());
            cell = row.createCell(24);
            cell.setCellValue(rowData.getType());
        }
        sheet.setAutoFilter(CellRangeAddress.valueOf("A1:Y1"));

    }

    private void populateTeamData(Sheet sheet, String teamType, CellStyle mainTextStyle) {
        setCurrentDateHeader(sheet);

        int rows = sheet.getLastRowNum();
        Row row;

        int updateRow = rows;
        int startColumn = 1;
        List<SinglesTeamAggregate> singlesTeamData = singlesDataTeamRepository.getAllByClassificationOrderByTotalDesc(teamType);
        for (SinglesTeamAggregate singlesTeamRowData : singlesTeamData) {
            row = sheet.createRow(++updateRow);
            addTeamData(row, startColumn, singlesTeamRowData.getTeam(), singlesTeamRowData.getTotal(), mainTextStyle);
        }

        if (!"Rookie".equals(teamType)) {
            startColumn += 3;

            updateRow = rows;
            List<HandicapTeamAggregate> handicapTeamData = handicapDataTeamRepository.getAllByClassificationOrderByTotalDesc(teamType);
            for (HandicapTeamAggregate handicapTeamRowData : handicapTeamData) {
                row = sheet.getRow(++updateRow);
                addTeamData(row, startColumn, handicapTeamRowData.getTeam(), handicapTeamRowData.getTotal(), mainTextStyle);
            }
            startColumn += 3;

            updateRow = rows;
            List<DoublesTeamAggregate> doublesTeamData = doublesDataTeamRepository.getAllByClassificationOrderByTotalDesc(teamType);
            for (DoublesTeamAggregate doublesTeamRowData : doublesTeamData) {
                row = sheet.getRow(++updateRow);
                addTeamData(row, startColumn, doublesTeamRowData.getTeam(), doublesTeamRowData.getTotal(), mainTextStyle);
            }
            startColumn += 3;

            updateRow = rows;
            List<SkeetTeamAggregate> skeetTeamData = skeetDataTeamRepository.getAllByClassificationOrderByTotalDesc(teamType);
            for (SkeetTeamAggregate skeetTeamRowData : skeetTeamData) {
                row = sheet.getRow(++updateRow);
                addTeamData(row, startColumn, skeetTeamRowData.getTeam(), skeetTeamRowData.getTotal(), mainTextStyle);
            }
            startColumn += 3;

            updateRow = rows;
            List<ClaysTeamAggregate> claysTeamData = claysDataTeamRepository.getAllByClassificationOrderByTotalDesc(teamType);
            for (ClaysTeamAggregate claysTeamRowData : claysTeamData) {
                row = sheet.getRow(++updateRow);
                addTeamData(row, startColumn, claysTeamRowData.getTeam(), claysTeamRowData.getTotal(), mainTextStyle);
            }
        }
    }

    private void populateIndividualData(Sheet sheet, String gender, CellStyle style, CellStyle mainTextStyle) {
        setCurrentDateHeader(sheet);

        int rows = sheet.getLastRowNum();
        Cell cell;
        Row row;

        int updateRow;
        int maxRow = rows;
        int classificationStartRow;
        boolean addBlankRowForHeader = false;
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

            List<SinglesAggregate> individualSinglesData = singlesRepository.getAllByGenderAndClassification(gender, classification);

            for (SinglesAggregate singlesRowData : individualSinglesData) {
                row = sheet.createRow(++updateRow);
                addPlayerData(row, column, singlesRowData.getAthlete(), singlesRowData.getTotal(), singlesRowData.getTeam(), mainTextStyle);
            }
            column += 4;
            maxRow = Math.max(maxRow, updateRow);

            updateRow = classificationStartRow;
            updateRow++;
            List<HandicapAggregate> individualHandicapData = handicapDataRepository.getAllByGenderAndClassification(gender, classification);
            for (HandicapAggregate handicapRowData : individualHandicapData) {
                row = sheet.getRow(++updateRow);
                addPlayerData(row, column, handicapRowData.getAthlete(), handicapRowData.getTotal(), handicapRowData.getTeam(), mainTextStyle);
            }
            column += 4;

            updateRow = classificationStartRow;
            updateRow++;
            List<DoublesAggregate> doublesIndividualData = doublesDataRepository.getAllByGenderAndClassification(gender, classification);
            for (DoublesAggregate doublesRowData : doublesIndividualData) {
                row = sheet.getRow(++updateRow);
                addPlayerData(row, column, doublesRowData.getAthlete(), doublesRowData.getTotal(), doublesRowData.getTeam(), mainTextStyle);
            }
            column += 4;

            updateRow = classificationStartRow;
            updateRow++;
            List<SkeetAggregate> skeetIndividualData = skeetDataRepository.getAllByGenderAndClassification(gender, classification);
            for (SkeetAggregate skeetRowData : skeetIndividualData) {
                row = sheet.getRow(++updateRow);
                addPlayerData(row, column, skeetRowData.getAthlete(), skeetRowData.getTotal(), skeetRowData.getTeam(), mainTextStyle);
            }
            column += 4;

            updateRow = classificationStartRow;
            updateRow++;
            List<ClaysAggregate> claysIndividualData = claysDataRepository.getAllByGenderAndClassification(gender, classification);
            for (ClaysAggregate claysRowData : claysIndividualData) {
                row = sheet.getRow(++updateRow);
                addPlayerData(row, column, claysRowData.getAthlete(), claysRowData.getTotal(), claysRowData.getTeam(), mainTextStyle);
            }
            maxRow = Math.max(maxRow, updateRow);
        }

        sheet.setAutoFilter(CellRangeAddress.valueOf("A13:T13"));
    }

    private void setCurrentDateHeader(Sheet sheet) {
        sheet.getRow(9).getCell(1).setCellValue(sheet.getRow(9).getCell(1).getStringCellValue() + currentDate);
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

    private static void autoSizeColumns(Workbook workbook) {
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int sheetNum = 1; sheetNum < numberOfSheets; sheetNum++) {
            Sheet sheet = workbook.getSheetAt(sheetNum);
            if (sheet.getPhysicalNumberOfRows() > 0) {
                Row row = sheet.getRow(sheet.getFirstRowNum());
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();
                    sheet.autoSizeColumn(columnIndex);
                    System.out.println(sheetNum + " column " + columnIndex);
                }
            }
        }
    }

}
