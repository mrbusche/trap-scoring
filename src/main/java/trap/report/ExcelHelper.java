package trap.report;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import trap.model.IndividualTotal;
import trap.model.RoundScore;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExcelHelper {

    static Logger logger = LoggerFactory.getLogger(ExcelHelper.class);

    public static CellStyle setFontForHeaders(Workbook workbook) {
        var font = workbook.createFont();
        font.setFontName("Calibri");
        font.setItalic(true);
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        var style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    public static CellStyle getCellStyle(Workbook workbook) {
        var mainText = workbook.createFont();
        mainText.setFontName("Calibri");
        mainText.setFontHeightInPoints((short) 12);
        var mainTextStyle = workbook.createCellStyle();
        mainTextStyle.setFont(mainText);
        return mainTextStyle;
    }

    public static void setCurrentDateHeader(Sheet sheet, String currentDate) {
        sheet.getRow(9).getCell(1).setCellValue(sheet.getRow(9).getCell(1).getStringCellValue() + currentDate);
    }

    public static void setCurrentSeasonHeader(Sheet sheet) {
        var cal = Calendar.getInstance();
        cal.setTime(new Date());
        var month = cal.get(Calendar.MONTH);
        var year = cal.get(Calendar.YEAR);
        var currentSeason = month > 8 ? year + 1 : year;
        sheet.getRow(8).getCell(1).setCellValue(currentSeason + " " + sheet.getRow(8).getCell(1).getStringCellValue());
    }

    public static void createFile(Workbook workbook) throws IOException {
        long start = System.currentTimeMillis();
        String filename = generateFilename();

        logger.info("Creating file: {}", filename);
        try (FileOutputStream fileOutputStream = new FileOutputStream(filename)) {
            logger.info("Writing file...");
            workbook.write(fileOutputStream);
            logger.info("File written successfully.");
        } catch (IOException e) {
            logger.error("Error writing file: {}", e.getMessage());
            throw e;
        }

        logger.info("Created file in {} ms", System.currentTimeMillis() - start);
    }

    private static String generateFilename() {
        var formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        var currentDate = formatter.format(new Date());
        return "league-data-" + currentDate + ".xlsx";
    }

    public static void addCleanData(Row row, RoundScore rowData) {
        row.createCell(0).setCellValue(rowData.eventId());
        row.createCell(1).setCellValue(rowData.event());
        row.createCell(2).setCellValue(rowData.locationId());
        row.createCell(3).setCellValue(rowData.location());
        row.createCell(4).setCellValue(rowData.eventDate());
        row.createCell(5).setCellValue(rowData.squadName());
        row.createCell(6).setCellValue(rowData.team());
        row.createCell(7).setCellValue(rowData.athlete());
        row.createCell(8).setCellValue(rowData.classification());
        row.createCell(9).setCellValue(rowData.gender());
        row.createCell(10).setCellValue(rowData.round1());
        row.createCell(11).setCellValue(rowData.round2());
        row.createCell(12).setCellValue(rowData.round3());
        row.createCell(13).setCellValue(rowData.round4());
        row.createCell(14).setCellValue(rowData.round5());
        row.createCell(15).setCellValue(rowData.round6());
        row.createCell(16).setCellValue(rowData.round7());
        row.createCell(17).setCellValue(rowData.round8());
        row.createCell(18).setCellValue(rowData.type());
    }

    public static void addTeamData(Row row, int startColumn, String team, int total, CellStyle mainTextStyle) {
        createAndStyleCell(row, startColumn, team, mainTextStyle);
        createAndStyleCell(row, startColumn + 1, total, mainTextStyle);
    }

    private static void createAndStyleCell(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);

        if (value instanceof String s) {
            cell.setCellValue(s);
        } else if (value instanceof Integer i) {
            cell.setCellValue(i);
        }

        cell.setCellStyle(style);
    }

    public static void addPlayerData(Row row, int column, String athlete, int total, String team, CellStyle mainTextStyle) {
        if (row == null) {
            throw new IllegalArgumentException("Row cannot be null");
        }

        createAndStyleCell(row, column, athlete, mainTextStyle);
        createAndStyleCell(row, column + 1, total, mainTextStyle);
        createAndStyleCell(row, column + 2, team, mainTextStyle);
    }

    public static int getRows(Sheet sheet, int rows, IndividualTotal rowData) {
        var row = sheet.createRow(++rows);
        var cell = row.createCell(0);
        generateTeamRows(rowData, row, cell);
        return rows;
    }

    public static void generateTeamRows(IndividualTotal rowData, Row row, Cell cell) {
        cell.setCellValue(rowData.type());
        cell = row.createCell(1);
        cell.setCellValue(rowData.team());
        cell = row.createCell(2);
        cell.setCellValue(rowData.classification());
        cell = row.createCell(3);
        cell.setCellValue(rowData.athlete());
        cell = row.createCell(4);
        cell.setCellValue(rowData.total());
    }
}
