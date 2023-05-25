package trap.report

import lombok.AccessLevel
import lombok.NoArgsConstructor
import org.apache.poi.ss.usermodel.*
import trap.model.IndividualTotal
import trap.model.RoundScore
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@NoArgsConstructor(access = AccessLevel.PRIVATE)
object ExcelHelper {
    @JvmStatic
    fun setFontForHeaders(workbook: Workbook): CellStyle {
        val font = workbook.createFont()
        font.fontName = "Calibri"
        font.italic = true
        font.bold = true
        font.fontHeightInPoints = 14.toShort()
        val style = workbook.createCellStyle()
        style.setFont(font)
        return style
    }

    @JvmStatic
    fun getCellStyle(workbook: Workbook): CellStyle {
        val mainText = workbook.createFont()
        mainText.fontName = "Calibri"
        mainText.fontHeightInPoints = 12.toShort()
        val mainTextStyle = workbook.createCellStyle()
        mainTextStyle.setFont(mainText)
        return mainTextStyle
    }

    @JvmStatic
    fun setCurrentDateHeader(sheet: Sheet, currentDate: String) {
        sheet.getRow(9).getCell(1).setCellValue(sheet.getRow(9).getCell(1).stringCellValue + currentDate)
    }

    @JvmStatic
    fun setCurrentSeasonHeader(sheet: Sheet) {
        val date = Date()
        val cal = Calendar.getInstance()
        cal.time = date
        val month = cal[Calendar.MONTH]
        val year = cal[Calendar.YEAR]
        val currentSeason = if (month > 8) year + 1 else year
        sheet.getRow(8).getCell(1).setCellValue(currentSeason.toString() + " " + sheet.getRow(8).getCell(1).stringCellValue)
    }

    @JvmStatic
    @Throws(IOException::class)
    fun createFile(workbook: Workbook, filename: String?) {
        val start: Long = System.currentTimeMillis()
        val date = Date()
        val formatter = SimpleDateFormat("yyyyMMddHHmmss")
        val currentDate = formatter.format(date)
        val newFilename = if (filename != null) "$filename.xlsx" else "league-data-$currentDate.xlsx"
        println("Creating file")
        val fileOutputStream = FileOutputStream(newFilename)
        println("Writing file")
        workbook.write(fileOutputStream)
        println("closing output stream")
        fileOutputStream.flush()
        fileOutputStream.close()
        println("Created file $newFilename")
        println("Wrote the contents to a file in " + (System.currentTimeMillis() - start) + "ms")
    }

    @JvmStatic
    fun addCleanData(row: Row, rowData: RoundScore) {
        var cell: Cell = row.createCell(0)
        cell.setCellValue(rowData.eventId.toDouble())
        cell = row.createCell(1)
        cell.setCellValue(rowData.event)
        cell = row.createCell(2)
        cell.setCellValue(rowData.locationId.toDouble())
        cell = row.createCell(3)
        cell.setCellValue(rowData.location)
        cell = row.createCell(4)
        cell.setCellValue(rowData.eventDate)
        cell = row.createCell(5)
        cell.setCellValue(rowData.squadName)
        cell = row.createCell(6)
        cell.setCellValue(rowData.team)
        cell = row.createCell(7)
        cell.setCellValue(rowData.athlete)
        cell = row.createCell(8)
        cell.setCellValue(rowData.classification)
        cell = row.createCell(9)
        cell.setCellValue(rowData.gender)
        cell = row.createCell(10)
        cell.setCellValue(rowData.round1.toDouble())
        cell = row.createCell(11)
        cell.setCellValue(rowData.round2.toDouble())
        cell = row.createCell(12)
        cell.setCellValue(rowData.round3.toDouble())
        cell = row.createCell(13)
        cell.setCellValue(rowData.round4.toDouble())
        cell = row.createCell(14)
        cell.setCellValue(rowData.round5.toDouble())
        cell = row.createCell(15)
        cell.setCellValue(rowData.round6.toDouble())
        cell = row.createCell(16)
        cell.setCellValue(rowData.round7.toDouble())
        cell = row.createCell(17)
        cell.setCellValue(rowData.round8.toDouble())
        cell = row.createCell(18)
        cell.setCellValue(rowData.type)
    }

    @JvmStatic
    fun addTeamData(row: Row, startColumn: Int, team: String?, total: Int, mainTextStyle: CellStyle?) {
        var cell = row.createCell(startColumn)
        cell.setCellValue(team)
        cell.cellStyle = mainTextStyle
        cell = row.createCell(startColumn + 1)
        cell.setCellValue(total.toDouble())
        cell.cellStyle = mainTextStyle
    }

    @JvmStatic
    fun addPlayerData(row: Row?, column: Int, athlete: String?, total: Int, team: String?, mainTextStyle: CellStyle?) {
        if (row != null) {
            var cell = row.createCell(column)
            cell.setCellValue(athlete)
            cell.cellStyle = mainTextStyle
            cell = row.createCell(column + 1)
            cell.setCellValue(total.toDouble())
            cell.cellStyle = mainTextStyle
            cell = row.createCell(column + 2)
            cell.setCellValue(team)
            cell.cellStyle = mainTextStyle
        }
    }

    @JvmStatic
    fun getRows(sheet: Sheet, rows: Int, rowData: IndividualTotal): Int {
        var rows = rows
        val row = sheet.createRow(++rows)
        var cell = row.createCell(0)
        cell.setCellValue(rowData.type)
        cell = row.createCell(1)
        cell.setCellValue(rowData.team)
        cell = row.createCell(2)
        cell.setCellValue(rowData.classification)
        cell = row.createCell(3)
        cell.setCellValue(rowData.athlete)
        cell = row.createCell(4)
        cell.setCellValue(rowData.total.toDouble())
        return rows
    }
}
