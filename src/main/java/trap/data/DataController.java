package trap.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class DataController {

    private static final Logger LOG = Logger.getLogger(DataController.class.getName());

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
    public DataController(JdbcTemplate jdbcTemplate) {
        jdbc = jdbcTemplate;
        String result = checkFileImport();
        if (result.contains("OFF")) {
            DataController.LOG.warning("local_infile is OFF.  File import is prohibited!");
        }
    }

    @RequestMapping("/checkFileImport")
    public String checkFileImport() {
        return jdbc.query("SHOW GLOBAL VARIABLES LIKE 'local_infile'", new Object[0], rs -> rs.next() ? rs.getString(1) + "=" + rs.getString(2) : "Count not determine value of local_infile");
    }

    @RequestMapping("/addData")
    public String addData() {
        try {
//            File singles = new File("C:\\trap\\singles.xls");
//            InputStream targetStream = new FileInputStream(singles);
//
//            InputStream singlesCsv = convertXlsToCSV(targetStream);
//            byte[] buffer = new byte[singlesCsv.available()];
//            singlesCsv.read(buffer);
//            File targetFile = new File("C:\\trap\\singles.csv");
//            OutputStream outputStream = new FileOutputStream(targetFile);
//            outputStream.write(buffer);

            return saveDataToDatabase();
        } catch (BadSqlGrammarException e) {
            e.printStackTrace();
            return "The MySQL database must allow local files to be imported. " + "From the mysql CLI, type 'set global local_infile=1'";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private String saveDataToDatabase() {
        DataController.LOG.fine("Saving trap data to database");

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

        fixNames();

        return results.toString();
    }

    private void fixNames() {
        jdbc.execute("UPDATE singles SET team = 'North Scott Trap Club' WHERE team = 'North Scott Trap Team';");
        jdbc.execute("UPDATE doubles SET team = 'North Scott Trap Club' WHERE team = 'North Scott Trap Team';");
        jdbc.execute("UPDATE handicap SET team = 'North Scott Trap Club' WHERE team = 'North Scott Trap Team';");
        jdbc.execute("UPDATE skeet SET team = 'North Scott Trap Club' WHERE team = 'North Scott Trap Team';");
        jdbc.execute("UPDATE clays SET team = 'North Scott Trap Club' WHERE team = 'North Scott Trap Team';");
    }

//    private InputStream convertXlsToCSV(InputStream inputStream) throws IOException {
//        Workbook wb = WorkbookFactory.create(inputStream);
//        return csvConverter(wb.getSheetAt(0));
//    }
//
//    private InputStream csvConverter(Sheet sheet) {
//        Row row;
//        StringBuilder str = new StringBuilder();
//        for (int i = 0; i < sheet.getLastRowNum() + 1; i++) {
//            row = sheet.getRow(i);
//            StringBuilder rowString = new StringBuilder();
//            for (int j = 0; j < 30; j++) {
//                if (row.getCell(j) == null) {
//                    rowString.append(" ").append(",");
//                } else {
//                    rowString.append(row.getCell(j).toString()).append(",");
//                }
//            }
//            str.append(rowString.substring(0, rowString.length() - 1)).append(System.lineSeparator());
//        }
//        System.out.println(str);
//        return new ByteArrayInputStream(str.toString().getBytes(StandardCharsets.UTF_8));
//    }

}
