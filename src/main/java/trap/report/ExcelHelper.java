package trap.report;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import trap.model.IndividualTotal;
import trap.model.RoundScore;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExcelHelper {
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
        var date = new Date();
        var cal = Calendar.getInstance();
        cal.setTime(date);
        var month = cal.get(Calendar.MONTH);
        var year = cal.get(Calendar.YEAR);
        var currentSeason = month > 8 ? year + 1 : year;
        sheet.getRow(8).getCell(1).setCellValue(currentSeason + " " + sheet.getRow(8).getCell(1).getStringCellValue());
    }

    public static void createFile(Workbook workbook) throws IOException {
        long start;
        start = System.currentTimeMillis();
        var date = new Date();
        var formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        var currentDate = formatter.format(date);
        var filename = "league-data-" + currentDate + ".xlsx";
        System.out.println("Creating file");
        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        System.out.println("Writing file");
        workbook.write(fileOutputStream);
        System.out.println("closing output stream");
        fileOutputStream.flush();
        fileOutputStream.close();
        System.out.println("Created file " + filename);
        System.out.println("Wrote the contents to a file in " + (System.currentTimeMillis() - start) + "ms");
    }

    public static void addCleanData(Row row, RoundScore rowData) {
        var cell = row.createCell(0);
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
    }

    public static void addTeamData(Row row, int startColumn, String team, int total, CellStyle mainTextStyle) {
        var cell = row.createCell(startColumn);
        cell.setCellValue(team);
        cell.setCellStyle(mainTextStyle);
        cell = row.createCell(startColumn + 1);
        cell.setCellValue(total);
        cell.setCellStyle(mainTextStyle);
    }

    public static void addPlayerData(Row row, int column, String athlete, int total, String team, CellStyle mainTextStyle) {
        if (row != null) {
            var cell = row.createCell(column);
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

    public static int getRows(Sheet sheet, int rows, IndividualTotal rowData) {
        var row = sheet.createRow(++rows);
        var cell = row.createCell(0);
        generateTeamRows(rowData, row, cell);
        return rows;
    }

    public static void generateTeamRows(IndividualTotal rowData, Row row, Cell cell) {
        cell.setCellValue(rowData.getType());
        cell = row.createCell(1);
        cell.setCellValue(rowData.getTeam());
        cell = row.createCell(2);
        cell.setCellValue(rowData.getClassification());
        cell = row.createCell(3);
        cell.setCellValue(rowData.getAthlete());
        cell = row.createCell(4);
        cell.setCellValue(rowData.getTotal());
    }
}
