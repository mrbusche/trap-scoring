package trap.report;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import trap.enums.Classifications;
import trap.enums.Genders;
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
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final List<String> classificationOptions = Stream.of(Classifications.values()).map(Classifications::name).collect(Collectors.toList());

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

    @RequestMapping(method = RequestMethod.GET, value = "/individual/{gender}")
    public String individualReports(@PathVariable(name = "gender") String gender) {
        StringBuilder results = new StringBuilder();
        results.append("<h1>Singles</h1>");
        for (String classification : classificationOptions) {
            List<SinglesAggregate> data = singlesRepository.getAllByClassificationAndGenderOrderByTotalDescAthleteAsc(Classifications.valueOf(classification).value, Genders.valueOf(gender.toUpperCase()).value);
            results.append("<h2>").append(Classifications.valueOf(classification)).append("</h2>");
            joinSinglesData(data, results);
        }

        results.append("<h1>Doubles</h1>");
        for (String classification : classificationOptions) {
            List<DoublesAggregate> data = doublesDataRepository.getAllByClassificationAndGenderOrderByTotalDescAthleteAsc(Classifications.valueOf(classification).value, Genders.valueOf(gender.toUpperCase()).value);
            //results.append("<h2>").append(Classifications.valueOf(classification)).append("</h2>");
            joinDoublesData(data, results);
        }

        results.append("<h1>Handicap</h1>");
        for (String classification : classificationOptions) {
            List<HandicapAggregate> data = handicapDataRepository.getAllByClassificationAndGenderOrderByTotalDescAthleteAsc(Classifications.valueOf(classification).value, Genders.valueOf(gender.toUpperCase()).value);
            //results.append("<h2>").append(Classifications.valueOf(classification)).append("</h2>");
            joinHandicapData(data, results);
        }

        results.append("<h1>Skeet</h1>");
        for (String classification : classificationOptions) {
            List<SkeetAggregate> data = skeetDataRepository.getAllByClassificationAndGenderOrderByTotalDescAthleteAsc(Classifications.valueOf(classification).value, Genders.valueOf(gender.toUpperCase()).value);
            //results.append("<h2>").append(Classifications.valueOf(classification)).append("</h2>");
            joinSkeetData(data, results);
        }

        return results.toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/school/{schoolName}")
    public String school(@PathVariable(name = "schoolName") String schoolName) {
        StringBuilder results = new StringBuilder();
        results.append("<h1>Singles</h1>");
        List<SinglesAggregate> data = singlesRepository.getAllByTeam(schoolName);
        joinSinglesData(data, results);

        results.append("<h1>Doubles</h1>");
        List<DoublesAggregate> doublesData = doublesDataRepository.getAllByTeam(schoolName);
        joinDoublesData(doublesData, results);

        results.append("<h1>Handicap</h1>");
        List<HandicapAggregate> handicapData = handicapDataRepository.getAllByTeam(schoolName);
        joinHandicapData(handicapData, results);

        results.append("<h1>Skeet</h1>");
        List<SkeetAggregate> skeetData = skeetDataRepository.getAllByTeam(schoolName);
        joinSkeetData(skeetData, results);

        return results.toString();
    }

    private static void joinSinglesData(List<SinglesAggregate> data, StringBuilder results) {
        results.append(data.stream().map(SinglesAggregate::toString).limit(25).collect(Collectors.joining("<br>")));
    }

    private static void joinDoublesData(List<DoublesAggregate> data, StringBuilder results) {
        results.append(data.stream().map(DoublesAggregate::toString).limit(25).collect(Collectors.joining("<br>")));
    }

    private static void joinHandicapData(List<HandicapAggregate> data, StringBuilder results) {
        results.append(data.stream().map(HandicapAggregate::toString).limit(25).collect(Collectors.joining("<br>")));
    }

    private static void joinSkeetData(List<SkeetAggregate> data, StringBuilder results) {
        results.append(data.stream().map(SkeetAggregate::toString).limit(25).collect(Collectors.joining("<br>")));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/team/{gender}")
    public String teamReports(@PathVariable(name = "gender") String gender) {
        StringBuilder results = new StringBuilder();
        results.append("<h1>Singles</h1>");
        for (String classification : classificationOptions) {
            List<SinglesTeamAggregate> data = singlesDataTeamRepository.getAllByClassificationOrderByTotalDesc(Classifications.valueOf(classification).value);
            //results.append("<h2>").append(Classifications.valueOf(classification)).append("</h2>");
            joinTeamData(data, results);
        }

        results.append("<h1>Doubles</h1>");
        for (String classification : classificationOptions) {
            List<DoublesTeamAggregate> data = doublesDataTeamRepository.getAllByClassificationOrderByTotalDesc(Classifications.valueOf(classification).value);
            //results.append("<h2>").append(Classifications.valueOf(classification)).append("</h2>");
            joinDoublesTeamData(data, results);
        }

        results.append("<h1>Handicap</h1>");
        for (String classification : classificationOptions) {
            List<HandicapTeamAggregate> data = handicapDataTeamRepository.getAllByClassificationOrderByTotalDesc(Classifications.valueOf(classification).value);
            //results.append("<h2>").append(Classifications.valueOf(classification)).append("</h2>");
            joinHandicapTeamData(data, results);
        }

        results.append("<h1>Skeet</h1>");
        for (String classification : classificationOptions) {
            List<SkeetTeamAggregate> data = skeetDataTeamRepository.getAllByClassificationOrderByTotalDesc(Classifications.valueOf(classification).value);
            //results.append("<h2>").append(Classifications.valueOf(classification)).append("</h2>");
            joinSkeetTeamData(data, results);
        }

        return results.toString();
    }

    private static void joinTeamData(List<SinglesTeamAggregate> data, StringBuilder results) {
        results.append(data.stream().map(SinglesTeamAggregate::toString).limit(10).collect(Collectors.joining("<br>")));
    }

    private static void joinDoublesTeamData(List<DoublesTeamAggregate> data, StringBuilder results) {
        results.append(data.stream().map(DoublesTeamAggregate::toString).limit(10).collect(Collectors.joining("<br>")));
    }

    private static void joinHandicapTeamData(List<HandicapTeamAggregate> data, StringBuilder results) {
        results.append(data.stream().map(HandicapTeamAggregate::toString).limit(10).collect(Collectors.joining("<br>")));
    }

    private static void joinSkeetTeamData(List<SkeetTeamAggregate> data, StringBuilder results) {
        results.append(data.stream().map(SkeetTeamAggregate::toString).limit(10).collect(Collectors.joining("<br>")));
    }

    @RequestMapping("/export")
    public String export() throws IOException {
        StringBuilder result = new StringBuilder();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("template.xls").getFile());
        Workbook workbook = WorkbookFactory.create(file);

        result.append("Workbook has ").append(workbook.getNumberOfSheets()).append(" sheets");
        workbook.forEach(sheet -> result.append("<br>").append(sheet.getSheetName()));

        long start = System.currentTimeMillis();
        populateCleanData(workbook);
        result.append("<br>Clean data populated in ").append(System.currentTimeMillis() - start).append("ms");

        start = System.currentTimeMillis();
        populateTeamData(workbook, "Senior");
        result.append("<br>Senior data populated in ").append(System.currentTimeMillis() - start).append("ms");

        start = System.currentTimeMillis();
        populateTeamData(workbook, "Intermediate Rookie");
        result.append("<br>Intermediate Rookie data populated in ").append(System.currentTimeMillis() - start).append("ms");

        start = System.currentTimeMillis();
        //autoSizeColumns(workbook);
        result.append("<br>Auto sized all columns in ").append(System.currentTimeMillis() - start).append("ms");

        FileOutputStream fileOutputStream = new FileOutputStream("updated.xls");
        workbook.write(fileOutputStream);
        fileOutputStream.close();

        workbook.close();

        return result.toString();
    }

    private void populateCleanData(Workbook workbook) {
        List<AllData> allData = allDataRepository.findAll();

        Sheet sheet = workbook.getSheet("Clean Data");
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
    }

    private void populateTeamData(Workbook workbook, String teamType) {
        Sheet sheet = workbook.getSheet(teamType);
        int rows = sheet.getLastRowNum();
        Cell cell;
        Row row;

        int updateRow = rows;
        List<SinglesTeamAggregate> singlesTeamData = singlesDataTeamRepository.getAllByClassificationOrderByTotalDesc(teamType);
        for (SinglesTeamAggregate rowData : singlesTeamData) {
            row = sheet.createRow(++updateRow);
            cell = row.createCell(0);
            cell.setCellValue(rowData.getTeam());
            cell = row.createCell(1);
            cell.setCellValue(rowData.getTotal());
        }

        updateRow = rows;
        List<HandicapTeamAggregate> handicapTeamData = handicapDataTeamRepository.getAllByClassificationOrderByTotalDesc(teamType);
        for (HandicapTeamAggregate rowData : handicapTeamData) {
            row = sheet.getRow(++updateRow);
            cell = row.createCell(3);
            cell.setCellValue(rowData.getTeam());
            cell = row.createCell(4);
            cell.setCellValue(rowData.getTotal());
        }

        updateRow = rows;
        List<DoublesTeamAggregate> doublesTeamData = doublesDataTeamRepository.getAllByClassificationOrderByTotalDesc(teamType);
        for (DoublesTeamAggregate rowData : doublesTeamData) {
            row = sheet.getRow(++updateRow);
            cell = row.createCell(6);
            cell.setCellValue(rowData.getTeam());
            cell = row.createCell(7);
            cell.setCellValue(rowData.getTotal());
        }

        updateRow = rows;
        List<SkeetTeamAggregate> skeetTeamData = skeetDataTeamRepository.getAllByClassificationOrderByTotalDesc(teamType);
        for (SkeetTeamAggregate rowData : skeetTeamData) {
            row = sheet.getRow(++updateRow);
            cell = row.createCell(9);
            cell.setCellValue(rowData.getTeam());
            cell = row.createCell(10);
            cell.setCellValue(rowData.getTotal());
        }
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
