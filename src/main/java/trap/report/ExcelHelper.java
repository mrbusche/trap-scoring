package trap.report;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExcelHelper {
    public CellStyle setFontForHeaders(Workbook workbook) {
        Font font = workbook.createFont();
        font.setFontName("Calibri");
        font.setItalic(true);
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    public CellStyle getCellStyle(Workbook workbook) {
        Font mainText = workbook.createFont();
        mainText.setFontName("Calibri");
        mainText.setFontHeightInPoints((short) 12);
        CellStyle mainTextStyle = workbook.createCellStyle();
        mainTextStyle.setFont(mainText);
        return mainTextStyle;
    }

    public void setCurrentDateHeader(Sheet sheet, String currentDate) {
        sheet.getRow(9).getCell(1).setCellValue(sheet.getRow(9).getCell(1).getStringCellValue() + currentDate);
    }

    public void setCurrentSeasonHeader(Sheet sheet) {
        java.util.Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int currentSeason = month > 8 ? year + 1 : year;
        sheet.getRow(8).getCell(1).setCellValue(currentSeason + " " + sheet.getRow(8).getCell(1).getStringCellValue());
    }

    public void createFile(Workbook workbook, String teamType) throws IOException {
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
