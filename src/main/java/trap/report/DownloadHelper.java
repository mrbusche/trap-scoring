package trap.report;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.io.FileUtils.copyURLToFile;

public class DownloadHelper {

    public void downloadFiles(String[] trapTypes) throws IOException, URISyntaxException {
        long start = System.currentTimeMillis();
        System.out.println("Started downloading files");

        Map<String, String> fileUrls = new HashMap<>();
        fileUrls.put("singles", "https://metabase.sssfonline.com/public/question/8648faf9-42e8-4a9c-b55d-2f251349de7f.csv");
        fileUrls.put("doubles", "https://metabase.sssfonline.com/public/question/5d5a78a5-2356-477f-b1b8-fe6ee11d25b1.csv");
        fileUrls.put("handicap", "https://metabase.sssfonline.com/public/question/69ca55d9-3e18-45bc-b57f-73aeb205ece8.csv");
        fileUrls.put("skeet", "https://metabase.sssfonline.com/public/question/c697d744-0e06-4c3f-a640-fea02f9c9ecd.csv");
        fileUrls.put("clays", "https://metabase.sssfonline.com/public/question/2c6edb1a-a7ee-43c2-8180-ad199a57be55.csv");
        fileUrls.put("fivestand", "https://metabase.sssfonline.com/public/question/3c5aecf2-a9f2-49b2-a11f-36965cb1a964.csv");
        fileUrls.put("doublesskeet", "https://metabase.sssfonline.com/public/question/bdd61066-6e29-4242-b6e9-adf286c2c4ae.csv");

        Charset charset = StandardCharsets.UTF_8;
        for (String type : trapTypes) {
            System.out.println("Downloading " + type + " file");
            copyURLToFile(new URI(fileUrls.get(type)).toURL(), new File(type + ".csv"), 60000, 60000);
            System.out.println("Finished downloading " + type + " file");

            System.out.println("Replacing double spaces for " + type + " file");
            Path path = Paths.get(type + ".csv");
            String content = Files.readString(path, charset);
            content = content.replaceAll(" {2}", " ");
            Files.writeString(path, content, charset);
            System.out.println("Finished replacing double spaces for " + type + " file");
        }
        System.out.println("Files downloaded in " + (System.currentTimeMillis() - start) + " ms");
    }
}
