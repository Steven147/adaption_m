package org.example.project

import io.github.bonigarcia.wdm.WebDriverManager
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.jsoup.Jsoup
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.chromium.ChromiumDriver
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.edge.EdgeOptions
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.Duration
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()
actual suspend fun openFileSaver(bytes: ByteArray, fileName: String) {
    val file = FileKit.openFileSaver(suggestedName = fileName)
    file?.write(bytes)
}

actual suspend fun getUrlsResult(
    excelFile: ByteArray,
): UrlsResult {
    val urls = readUrlsFromExcel(excelFile)
    return UrlsResult(urls)
}

actual suspend fun getUrlsMapResult(
    urlsResult: UrlsResult,
): UrlsMapResult {
    val urlsMap = urlsResult.urls.mapNotNull { url ->
        crawlImageUrls(url).let { imgUrl ->
            imgUrl?.let {
                url to imgUrl
            }
        }
    }.toMap()
    return UrlsMapResult(urlsMap)
}


actual suspend fun getBytes(urlsResult: UrlsResult): ByteArray {
    return zipImages(urlsResult) {
        openFileSaver(it, "result.zip")
    }
}


// 从 Excel 读取 URL
fun readUrlsFromExcel(bytes: ByteArray): List<String> {
    ByteArrayInputStream(bytes).use { input ->
        XSSFWorkbook(input).use { workbook ->
            val sheet = workbook.getSheetAt(0)
            return sheet.flatMap { row ->
                (0 until row.lastCellNum).mapNotNull { cellIndex ->
                    row.getCell(cellIndex)?.let { cell ->
                        DataFormatter().formatCellValue(cell).takeIf {
                            it.startsWith("http", ignoreCase = true)
                        }
                    }
                }
            }
        }
    }
}


private val driver by lazy {
    WebDriverManager.edgedriver().setup()
    return@lazy EdgeDriver(EdgeOptions().apply {
        addArguments("--headless=new")
        addArguments("--disable-gpu")
        addArguments("--no-sandbox")
        addArguments("--disable-logging")  // 禁用浏览器驱动日志
        addArguments("--warning")
        setCapability("goog:loggingPrefs", mapOf("browser" to "OFF"))
    })
}

suspend fun zipImages(urlsResult: UrlsResult, block: suspend (ByteArray)->Unit): ByteArray {
    val zipBytes = ByteArrayOutputStream().use { output ->
        ZipOutputStream(output).use { zipOut ->
            urlsResult.urls.forEach { url ->
                downloadImage(url).let {
                    withContext(Dispatchers.IO) {
                        val entry = ZipEntry("image/${url.replace('/', '_')}.jpg")
                        zipOut.putNextEntry(entry)
                        zipOut.write(it)
                        zipOut.closeEntry()
                    }
                }
            }
            driver.quit()
        }
        output.toByteArray().also {
            block(it)
        }
    }
    return zipBytes
}



// 下载单张图片
private suspend fun downloadImage(url: String): ByteArray {
    return Jsoup.connect(url)
        .ignoreContentType(true)
        .maxBodySize(0)
        .execute()
        .bodyAsBytes()
}



// 爬取单个页面的图片
suspend fun crawlImageUrls(url: String): String? {
    return try {
        var retryCount = 0

        while (retryCount < 5) {
            println("[crawlImageUrls] begin retry: $retryCount")
            retryCount+=1
            driver.get(url)

//        val doc = Jsoup.connect(url)
//            .userAgent(USER_AGENT)
//            .headers(HEADERS)
//            .header("Accept", ACCEPT_HEADER)
//            .timeout(15000)
//            .ignoreContentType(true)
//            .followRedirects(true)
//            .execute().parse()


            // 使用显式等待替代固定延迟
            WebDriverWait(driver, Duration.ofSeconds(10)).until {
                (it as JavascriptExecutor).executeScript(
                    """
                return document.readyState === 'complete'
                """
                ) as Boolean
            }

//            delay(5000)
            val doc = Jsoup.parse(driver.pageSource)
            doc.select("""
                      div[class*='mainPicWrap'] > img,
                      div[data-spm-anchor-id] > img[src*='img.alicdn.com']
                   """.trimIndent()).first()?.let { img ->
                val imgUrl = img.absUrl("src")
                println("[crawlImageUrls] imgUrl: $imgUrl")
                return imgUrl
            }
        }
        println("[crawlImageUrls] img null")
        null
    } catch (e: Exception) {
        println("[crawlImageUrls] failed")
        null
    } finally {
        println("[crawlImageUrls] quit1")
    }
}
