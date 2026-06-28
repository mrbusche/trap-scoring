package trap.report;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trap.model.IndividualTotal;
import trap.model.RoundScore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExcelHelperTest {

    private Workbook workbook;
    private Sheet sheet;

    @BeforeEach
    void setUp() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Test Sheet");
    }

    @AfterEach
    void tearDown() throws IOException {
        workbook.close();
    }

    @Test
    void testSetFontForHeaders() {
        CellStyle style = ExcelHelper.setFontForHeaders(workbook);
        Font font = workbook.getFontAt(style.getFontIndex());

        assertAll("Header font styling properties",
                () -> assertNotNull(style),
                () -> assertEquals("Calibri", font.getFontName()),
                () -> assertTrue(font.getItalic()),
                () -> assertTrue(font.getBold()),
                () -> assertEquals(14, font.getFontHeightInPoints())
        );
    }

    @Test
    void testGetCellStyle() {
        CellStyle style = ExcelHelper.getCellStyle(workbook);
        Font font = workbook.getFontAt(style.getFontIndex());

        assertAll("Main text font styling properties",
                () -> assertNotNull(style),
                () -> assertEquals("Calibri", font.getFontName()),
                () -> assertEquals(12, font.getFontHeightInPoints())
        );
    }

    @Test
    void testSetCurrentDateHeader() {
        Row row = sheet.createRow(9);
        Cell cell = row.createCell(1);
        cell.setCellValue("Generated on: ");

        ExcelHelper.setCurrentDateHeader(sheet, "06/20/2026");

        assertEquals("Generated on: 06/20/2026", cell.getStringCellValue());
    }

    @Test
    void testSetCurrentSeasonHeader() {
        Row row = sheet.createRow(8);
        Cell cell = row.createCell(1);
        cell.setCellValue("Season Standings");

        ExcelHelper.setCurrentSeasonHeader(sheet, 8);

        LocalDate now = LocalDate.now();
        int expectedYear = now.getMonthValue() > 8 ? now.getYear() + 1 : now.getYear();

        assertEquals(expectedYear + " Season Standings", cell.getStringCellValue());
    }

    @Test
    void testAddCleanData() {
        Row row = sheet.createRow(0);

        // Using Mockito to bypass needing exact record/class constructors for the model
        RoundScore mockScore = mock(RoundScore.class);
        when(mockScore.eventId()).thenReturn(101);
        when(mockScore.event()).thenReturn("Spring League");
        when(mockScore.locationId()).thenReturn(50);
        when(mockScore.location()).thenReturn("Local Gun Club");
        when(mockScore.eventDate()).thenReturn("2026-06-20");
        when(mockScore.squadName()).thenReturn("Alpha Squad");
        when(mockScore.team()).thenReturn("Dream Team");
        when(mockScore.athlete()).thenReturn("John Doe");
        when(mockScore.classification()).thenReturn("Varsity");
        when(mockScore.gender()).thenReturn("M");
        when(mockScore.round1()).thenReturn(25);
        when(mockScore.round2()).thenReturn(24);
        when(mockScore.round3()).thenReturn(23);
        when(mockScore.round4()).thenReturn(22);
        when(mockScore.round5()).thenReturn(21);
        when(mockScore.round6()).thenReturn(20);
        when(mockScore.round7()).thenReturn(19);
        when(mockScore.round8()).thenReturn(18);
        when(mockScore.type()).thenReturn("singles");

        ExcelHelper.addCleanData(row, mockScore);

        assertAll("Clean data row mapping",
                () -> assertEquals(101.0, row.getCell(0).getNumericCellValue()),
                () -> assertEquals("Spring League", row.getCell(1).getStringCellValue()),
                () -> assertEquals(50.0, row.getCell(2).getNumericCellValue()),
                () -> assertEquals("Local Gun Club", row.getCell(3).getStringCellValue()),
                () -> assertEquals("2026-06-20", row.getCell(4).getStringCellValue()),
                () -> assertEquals("Dream Team", row.getCell(6).getStringCellValue()),
                () -> assertEquals("John Doe", row.getCell(7).getStringCellValue()),
                () -> assertEquals(25.0, row.getCell(10).getNumericCellValue()),
                () -> assertEquals(18.0, row.getCell(17).getNumericCellValue()),
                () -> assertEquals("singles", row.getCell(18).getStringCellValue())
        );
    }

    @Test
    void testAddTeamData() {
        Row row = sheet.createRow(0);
        CellStyle style = ExcelHelper.getCellStyle(workbook);

        ExcelHelper.addTeamData(row, 2, "Tigers", 245, style);

        assertAll("Team data insertion logic",
                () -> assertEquals("Tigers", row.getCell(2).getStringCellValue()),
                () -> assertEquals(245.0, row.getCell(3).getNumericCellValue()),
                () -> assertEquals(style, row.getCell(2).getCellStyle()),
                () -> assertEquals(style, row.getCell(3).getCellStyle())
        );
    }

    @Test
    void testAddPlayerData() {
        Row row = sheet.createRow(0);
        CellStyle style = ExcelHelper.getCellStyle(workbook);

        ExcelHelper.addPlayerData(row, 1, "Jane Smith", 98, "Lions", style);

        assertAll("Player data insertion logic",
                () -> assertEquals("Jane Smith", row.getCell(1).getStringCellValue()),
                () -> assertEquals(98.0, row.getCell(2).getNumericCellValue()),
                () -> assertEquals("Lions", row.getCell(3).getStringCellValue()),
                () -> assertEquals(style, row.getCell(1).getCellStyle()),
                () -> assertEquals(style, row.getCell(2).getCellStyle()),
                () -> assertEquals(style, row.getCell(3).getCellStyle())
        );
    }

    @Test
    void testAddPlayerDataThrowsOnNullRow() {
        CellStyle style = ExcelHelper.getCellStyle(workbook);

        // JUnit 6 continues to support assertThrows cleanly
        assertThrows(IllegalArgumentException.class,
                () -> ExcelHelper.addPlayerData(null, 1, "Jane Smith", 98, "Lions", style));
    }

    @Test
    void testGetRowsAndGenerateTeamRows() {
        IndividualTotal mockTotal = mock(IndividualTotal.class);
        when(mockTotal.type()).thenReturn("doubles");
        when(mockTotal.team()).thenReturn("Hawks");
        when(mockTotal.classification()).thenReturn("Junior Varsity");
        when(mockTotal.athlete()).thenReturn("Mike Brown");
        when(mockTotal.total()).thenReturn(150);

        int initialRowIndex = 5;
        int nextRowIndex = ExcelHelper.getRows(sheet, initialRowIndex, mockTotal);

        assertEquals(6, nextRowIndex, "Row index should increment by 1");
        Row row = sheet.getRow(6);

        assertAll("Generated team rows from IndividualTotal mapping",
                () -> assertNotNull(row),
                () -> assertEquals("doubles", row.getCell(0).getStringCellValue()),
                () -> assertEquals("Hawks", row.getCell(1).getStringCellValue()),
                () -> assertEquals("Junior Varsity", row.getCell(2).getStringCellValue()),
                () -> assertEquals("Mike Brown", row.getCell(3).getStringCellValue()),
                () -> assertEquals(150.0, row.getCell(4).getNumericCellValue())
        );
    }

    @Test
    void testCreateFile() throws IOException {
        String filename = "league-data-test.xlsx";
        ExcelHelper.createFile(workbook, filename);

        Path generatedFile = Path.of(filename);
        assertTrue(Files.exists(generatedFile), "Excel file should have been created on disk");
        Files.deleteIfExists(generatedFile);
    }
}
