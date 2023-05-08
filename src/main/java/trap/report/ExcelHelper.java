package trap.report;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
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
        Font font = workbook.createFont();
        font.setFontName("Calibri");
        font.setItalic(true);
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    public static CellStyle getCellStyle(Workbook workbook) {
        Font mainText = workbook.createFont();
        mainText.setFontName("Calibri");
        mainText.setFontHeightInPoints((short) 12);
        CellStyle mainTextStyle = workbook.createCellStyle();
        mainTextStyle.setFont(mainText);
        return mainTextStyle;
    }

    public static void setCurrentDateHeader(Sheet sheet, String currentDate) {
        sheet.getRow(9).getCell(1).setCellValue(sheet.getRow(9).getCell(1).getStringCellValue() + currentDate);
    }

    public static void setCurrentSeasonHeader(Sheet sheet) {
        java.util.Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int currentSeason = month > 8 ? year + 1 : year;
        sheet.getRow(8).getCell(1).setCellValue(currentSeason + " " + sheet.getRow(8).getCell(1).getStringCellValue());
    }

    public static void createFile(Workbook workbook, String teamType) throws IOException {
        long start;
        start = System.currentTimeMillis();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDate = formatter.format(date);
        String newFilename = teamType + "-" + currentDate + ".xlsx";
        System.out.println("Creating file");
        FileOutputStream fileOutputStream = new FileOutputStream(newFilename);
        System.out.println("Writing file");
        workbook.write(fileOutputStream);
        System.out.println("closing output stream");
        fileOutputStream.close();
        System.out.println("Created file " + newFilename);
        System.out.println("Wrote the contents to a file in " + (System.currentTimeMillis() - start) + "ms");
    }

    public static void addCleanData(Row row, RoundScore rowData) {
        Cell cell;
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
    }

    public static void addTeamData(Row row, int startColumn, String team, Integer total, CellStyle mainTextStyle) {
        Cell cell = row.createCell(startColumn);
        cell.setCellValue(team);
        cell.setCellStyle(mainTextStyle);
        cell = row.createCell(startColumn + 1);
        cell.setCellValue(total);
        cell.setCellStyle(mainTextStyle);
    }

    public static void addPlayerData(Row row, int column, String athlete, Integer total, String team, CellStyle mainTextStyle) {
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

    public static int getRows(Sheet sheet, int rows, IndividualTotal rowData) {
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
}
