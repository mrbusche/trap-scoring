package trap.report

import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class DownloadHelper {
    @Throws(IOException::class, URISyntaxException::class)
    fun downloadFiles(trapTypes: Array<String>) {
        val start = System.currentTimeMillis()
        println("Started downloading files")
        val fileUrls: MutableMap<String, String> = HashMap()
        fileUrls["singles"] = "https://metabase.sssfonline.com/public/question/8648faf9-42e8-4a9c-b55d-2f251349de7f.csv"
        fileUrls["doubles"] = "https://metabase.sssfonline.com/public/question/5d5a78a5-2356-477f-b1b8-fe6ee11d25b1.csv"
        fileUrls["handicap"] = "https://metabase.sssfonline.com/public/question/69ca55d9-3e18-45bc-b57f-73aeb205ece8.csv"
        fileUrls["skeet"] = "https://metabase.sssfonline.com/public/question/c697d744-0e06-4c3f-a640-fea02f9c9ecd.csv"
        fileUrls["clays"] = "https://metabase.sssfonline.com/public/question/2c6edb1a-a7ee-43c2-8180-ad199a57be55.csv"
        fileUrls["fivestand"] = "https://metabase.sssfonline.com/public/question/3c5aecf2-a9f2-49b2-a11f-36965cb1a964.csv"
        fileUrls["doublesskeet"] = "https://metabase.sssfonline.com/public/question/bdd61066-6e29-4242-b6e9-adf286c2c4ae.csv"
        val charset = StandardCharsets.UTF_8
        for (type in trapTypes) {
            println("Downloading $type file")
            FileUtils.copyURLToFile(URI(fileUrls[type]).toURL(), File("$type.csv"), 60000, 60000)
            println("Finished downloading $type file")
            println("Replacing double spaces for $type file")
            val path = Paths.get("$type.csv")
            var content = Files.readString(path, charset)
            content = content.replace(" {2}".toRegex(), " ")
            Files.writeString(path, content, charset)
            println("Finished replacing double spaces for $type file")
        }
        println("Files downloaded in " + (System.currentTimeMillis() - start) + " ms")
    }
}
