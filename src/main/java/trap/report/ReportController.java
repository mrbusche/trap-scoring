package trap.report;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trap.model.AllData;
import trap.model.DoublesAggregate;
import trap.model.DoublesTeamAggregate;
import trap.model.HandicapAggregate;
import trap.model.HandicapTeamAggregate;
import trap.model.SinglesAggregate;
import trap.model.SinglesTeamAggregate;
import trap.model.SkeetAggregate;
import trap.model.SkeetTeamAggregate;
import trap.repository.AllDataRepository;
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

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
    private final AllDataRepository allDataRepository;

    //Senior, Intermediate, Rookie, Collegiate
    private final List<String> classificationList = Arrays.asList("Senior", "Intermediate", "Rookie", "Collegiate");

    @Autowired
    public ReportController(SinglesDataRepository singlesRepository, SinglesDataTeamRepository singlesDataTeamRepository, DoublesDataRepository doublesDataRepository, DoublesDataTeamRepository doublesDataTeamRepository, HandicapDataRepository handicapDataRepository, HandicapDataTeamRepository handicapDataTeamRepository, SkeetDataRepository skeetDataRepository, SkeetDataTeamRepository skeetDataTeamRepository, AllDataRepository allDataRepository) {
        this.singlesRepository = singlesRepository;
        this.singlesDataTeamRepository = singlesDataTeamRepository;
        this.doublesDataRepository = doublesDataRepository;
        this.doublesDataTeamRepository = doublesDataTeamRepository;
        this.handicapDataRepository = handicapDataRepository;
        this.handicapDataTeamRepository = handicapDataTeamRepository;
        this.skeetDataRepository = skeetDataRepository;
        this.skeetDataTeamRepository = skeetDataTeamRepository;
        this.allDataRepository = allDataRepository;
    }

    @RequestMapping("/export")
    public String export() throws IOException {
        StringBuilder result = new StringBuilder();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("template.xls")).getFile());
        Workbook workbook = WorkbookFactory.create(file);

        result.append("Workbook has ").append(workbook.getNumberOfSheets()).append(" sheets");
        workbook.forEach(sheet -> result.append("<br>").append(sheet.getSheetName()));

        long start = System.currentTimeMillis();
        populateCleanData(workbook.getSheet("Clean Data"));
        result.append("<br>Clean data populated in ").append(System.currentTimeMillis() - start).append("ms");

        start = System.currentTimeMillis();
        populateTeamData(workbook.getSheet("Team-Senior"), "Senior");
        result.append("<br>Senior data populated in ").append(System.currentTimeMillis() - start).append("ms");

        start = System.currentTimeMillis();
        populateTeamData(workbook.getSheet("Team-Intermediate"), "Intermediate");
        result.append("<br>Intermediate data populated in ").append(System.currentTimeMillis() - start).append("ms");

        start = System.currentTimeMillis();
        populateTeamData(workbook.getSheet("Team-Rookie"), "Rookie");
        result.append("<br>Rookie data populated in ").append(System.currentTimeMillis() - start).append("ms");

        start = System.currentTimeMillis();
        populateIndividualData(workbook.getSheet("Individual-Men"), "M");
        result.append("<br>Individual Men data populated in ").append(System.currentTimeMillis() - start).append("ms");

        start = System.currentTimeMillis();
        populateIndividualData(workbook.getSheet("Individual-Ladies"), "F");
        result.append("<br>Individual Women data populated in ").append(System.currentTimeMillis() - start).append("ms");

        start = System.currentTimeMillis();
        autoSizeColumns(workbook);
        result.append("<br>Auto sized all columns in ").append(System.currentTimeMillis() - start).append("ms");

        FileOutputStream fileOutputStream = new FileOutputStream("updated.xls");
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
            cell.setCellValue(rowData.getShirtsize());
            cell = row.createCell(24);
            cell.setCellValue(rowData.getAtaid());
            cell = row.createCell(25);
            cell.setCellValue(rowData.getNssaid());
            cell = row.createCell(26);
            cell.setCellValue(rowData.getNscaid());
            cell = row.createCell(27);
            cell.setCellValue(rowData.getSctppayment());
            cell = row.createCell(28);
            cell.setCellValue(rowData.getSctpconsent());
            cell = row.createCell(29);
            cell.setCellValue(rowData.getAtapayment());
            cell = row.createCell(30);
            cell.setCellValue(rowData.getNscapayment());
            cell = row.createCell(31);
            cell.setCellValue(rowData.getNssapayment());
            cell = row.createCell(32);
            cell.setCellValue(rowData.getType());
        }
        sheet.setAutoFilter(CellRangeAddress.valueOf("A1:AG1"));

    }

    private void populateTeamData(Sheet sheet, String teamType) {
        int rows = sheet.getLastRowNum();
        Cell cell;
        Row row;

        int updateRow = rows;
        int startColumn = 1;
        List<SinglesTeamAggregate> singlesTeamData = singlesDataTeamRepository.getAllByClassificationOrderByTotalDesc(teamType);
        for (SinglesTeamAggregate singlesTeamRowData : singlesTeamData) {
            row = sheet.createRow(++updateRow);
            cell = row.createCell(startColumn);
            cell.setCellValue(singlesTeamRowData.getTeam());
            cell = row.createCell(startColumn + 1);
            cell.setCellValue(singlesTeamRowData.getTotal());
        }
        startColumn += 3;

        updateRow = rows;
        List<HandicapTeamAggregate> handicapTeamData = handicapDataTeamRepository.getAllByClassificationOrderByTotalDesc(teamType);
        for (HandicapTeamAggregate handicapTeamRowData : handicapTeamData) {
            row = sheet.getRow(++updateRow);
            cell = row.createCell(startColumn);
            cell.setCellValue(handicapTeamRowData.getTeam());
            cell = row.createCell(startColumn + 1);
            cell.setCellValue(handicapTeamRowData.getTotal());
        }
        startColumn += 3;

        updateRow = rows;
        List<DoublesTeamAggregate> doublesTeamData = doublesDataTeamRepository.getAllByClassificationOrderByTotalDesc(teamType);
        for (DoublesTeamAggregate doublesTeamRowData : doublesTeamData) {
            row = sheet.getRow(++updateRow);
            cell = row.createCell(startColumn);
            cell.setCellValue(doublesTeamRowData.getTeam());
            cell = row.createCell(startColumn + 1);
            cell.setCellValue(doublesTeamRowData.getTotal());
        }
        startColumn += 3;

        updateRow = rows;
        List<SkeetTeamAggregate> skeetTeamData = skeetDataTeamRepository.getAllByClassificationOrderByTotalDesc(teamType);
        for (SkeetTeamAggregate skeetTeamRowData : skeetTeamData) {
            row = sheet.getRow(++updateRow);
            cell = row.createCell(startColumn);
            cell.setCellValue(skeetTeamRowData.getTeam());
            cell = row.createCell(startColumn + 1);
            cell.setCellValue(skeetTeamRowData.getTotal());
        }
    }

    private void populateIndividualData(Sheet sheet, String gender) {
        int rows = sheet.getLastRowNum();
        Cell cell;
        Row row;

        int updateRow;
        int maxRow = rows;
        System.out.println("max row being set::" + rows);
        int classificationStartRow;
        for (String classification : classificationList) {
            System.out.println("classification::" + classification);
            System.out.println("maxRow var::" + maxRow);
            int column = 1;
            updateRow = maxRow;
            classificationStartRow = updateRow;
            //Add row headers
            row = sheet.createRow(++updateRow);
            cell = row.createCell(column);
            cell.setCellValue(classification + " Division");
            cell = row.createCell(column + 4);
            cell.setCellValue(classification + " Division");
            cell = row.createCell(column + 8);
            cell.setCellValue(classification + " Division");
            cell = row.createCell(column + 12);
            cell.setCellValue(classification + " Division");
            cell = row.createCell(column + 16);
            cell.setCellValue(classification + " Division");

            System.out.println("updateRow equal maxRow::" + updateRow);
            List<SinglesAggregate> individualSinglesData = singlesRepository.getAllByGenderAndClassification(gender, classification);
            System.out.println("individualSinglesData count::" + individualSinglesData.size());

            for (SinglesAggregate singlesRowData : individualSinglesData) {
                row = sheet.createRow(++updateRow);
                cell = row.createCell(column);
                cell.setCellValue(singlesRowData.getAthlete());
                cell = row.createCell(column + 1);
                cell.setCellValue(singlesRowData.getTotal());
                cell = row.createCell(column + 2);
                cell.setCellValue(singlesRowData.getTeam());
            }
            column += 4;
            maxRow = Math.max(maxRow, updateRow);

            updateRow = classificationStartRow;
            updateRow++;

            List<HandicapAggregate> individualHandicapData = handicapDataRepository.getAllByGenderAndClassification(gender, classification);
            System.out.println("individualHandicapData count::" + individualHandicapData.size());
            for (HandicapAggregate handicapRowData : individualHandicapData) {
                row = sheet.getRow(++updateRow);
                cell = row.createCell(column);
                cell.setCellValue(handicapRowData.getAthlete());
                cell = row.createCell(column + 1);
                cell.setCellValue(handicapRowData.getTotal());
                cell = row.createCell(column + 2);
                cell.setCellValue(handicapRowData.getTeam());

            }
            column += 4;

            updateRow = classificationStartRow;
            updateRow++;
            List<DoublesAggregate> doublesIndividualData = doublesDataRepository.getAllByGenderAndClassification(gender, classification);
            System.out.println("doublesIndividualData count::" + doublesIndividualData.size());
            for (DoublesAggregate handicapRowData : doublesIndividualData) {
                row = sheet.getRow(++updateRow);
                cell = row.createCell(column);
                cell.setCellValue(handicapRowData.getAthlete());
                cell = row.createCell(column + 1);
                cell.setCellValue(handicapRowData.getTotal());
                cell = row.createCell(column + 2);
                cell.setCellValue(handicapRowData.getTeam());
            }
            column += 4;

            updateRow = classificationStartRow;
            updateRow++;
            List<SkeetAggregate> skeetIndividualData = skeetDataRepository.getAllByGenderAndClassification(gender, classification);
            System.out.println("skeetIndividualData count::" + skeetIndividualData.size());
            for (SkeetAggregate handicapRowData : skeetIndividualData) {
                row = sheet.getRow(++updateRow);
                cell = row.createCell(column);
                cell.setCellValue(handicapRowData.getAthlete());
                cell = row.createCell(column + 1);
                cell.setCellValue(handicapRowData.getTotal());
                cell = row.createCell(column + 2);
                cell.setCellValue(handicapRowData.getTeam());
            }
            maxRow = Math.max(maxRow, updateRow);
        }

        sheet.setAutoFilter(CellRangeAddress.valueOf("A2:X2"));
    }

    private static void autoSizeColumns(Workbook workbook) {
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int sheetNum = 0; sheetNum < numberOfSheets; sheetNum++) {
            Sheet sheet = workbook.getSheetAt(sheetNum);
            if (sheet.getPhysicalNumberOfRows() > 0) {
                Row row = sheet.getRow(sheet.getFirstRowNum());
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();
                    sheet.autoSizeColumn(columnIndex);
                }
            }
        }
    }

}
