package org.example.project

import io.github.bonigarcia.wdm.WebDriverManager
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.net.URL
import java.util.UUID
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.chromium.ChromiumDriver
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.edge.EdgeOptions
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.http.HttpHeaders
import java.time.Duration

// 更新爬虫参数配置
private const val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
private const val ACCEPT_HEADER = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"

// 新增头信息
private val HEADERS = mapOf(
    "Accept-Language" to "zh-CN,zh;q=0.9",
    "Accept-Encoding" to "gzip, deflate, br",
    "Connection" to "keep-alive",
    "Sec-Fetch-Mode" to "navigate",
    "Sec-Fetch-Site" to "same-origin"
)
val image_dir = "your_image_directory" // 请替换为实际的图片目录
val prefix_url = "/static/images/"



fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "127.0.0.1", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(StatusPages) {
        exception<IllegalStateException> { call, cause ->
            call.respondText("App in illegal state as ${cause.message}")
        }
    }
    install(ContentNegotiation) {
        json()
    }

    install(CORS) {
        anyHost()
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
    }
    routing {
        get("/images/{count}") {
            val count = call.parameters["count"]?.toIntOrNull() ?: return@get call.respondText("Invalid count parameter", status = io.ktor.http.HttpStatusCode.BadRequest)
            val files = File(image_dir).listFiles()?.filter { it.name.endsWith(".jpg") }?.map { "${prefix_url}${it.name}" } ?: emptyList()
            if (files.size < count) {
                call.respondText("File not enough", status = io.ktor.http.HttpStatusCode.NotFound)
            } else {
                val randomUrls = files.shuffled().take(count)
                call.respond(mapOf("image_urls" to randomUrls))
            }
        }
        get("$prefix_url{name}") {
            val name = call.parameters["name"] ?: return@get call.respondText("Missing name parameter", status = io.ktor.http.HttpStatusCode.BadRequest)
            val filepath = File("$image_dir/$name")
            if (filepath.exists()) {
                call.respondFile(filepath)
            } else {
                call.respondText("File not found", status = io.ktor.http.HttpStatusCode.NotFound)
            }
        }
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }


        post(READ_EXCEL) {
            val multipart = call.receiveMultipart()
            var excelFile: File? = null

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        println("收到表单字段: ${part.name}")
                    }
                    is PartData.FileItem -> {
                        val ext = File(part.originalFileName).extension
                        val file = File("upload.$ext")
                        part.streamProvider().use { input ->
                            file.outputStream().buffered().use { output ->
                                input.copyToSuspend(output)
                            }
                        }
                        excelFile = file
                    }
                    else -> println("收到其他类型的部分: ${part.name}")
                }
                part.dispose()
            }

            excelFile?.readBytes()?.let { bytes ->
                val urls = readUrlsFromExcel(bytes)
                call.respond(UrlsResult(urls))
            } ?: call.respond(HttpStatusCode.BadRequest)
        }

        post(START_CRAWL) {
            val urlsResult = call.receive<UrlsResult>()

            val resultsMap = urlsResult.urls.associateWith {
                url -> (crawlImageUrls(url) ?: "")
            }

            call.respond(UrlsMapResult(resultsMap))
        }


        post(DOWNLOAD_ZIP) {
            val urlsResult = call.receive<UrlsResult>()
            val zipBytes = zipImages(urlsResult) {}

            call.respondBytes(
                bytes = zipBytes,
                contentType = ContentType.Application.Zip,
                status = HttpStatusCode.OK
            )
        }

    }
}

suspend fun InputStream.copyToSuspend(
    out: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    yieldSize: Int = 4 * 1024 * 1024,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Long {
    return withContext(dispatcher) {
        val buffer = ByteArray(bufferSize)
        var bytesCopied = 0L
        var bytesAfterYield = 0L
        while (true) {
            val bytes = read(buffer).takeIf { it >= 0 } ?: break
            out.write(buffer, 0, bytes)
            if (bytesAfterYield >= yieldSize) {
                yield()
                bytesAfterYield %= yieldSize
            }
            bytesCopied += bytes
            bytesAfterYield += bytes
        }
        return@withContext bytesCopied
    }
}