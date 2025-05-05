import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.example.project.DOWNLOAD_ZIP
import org.example.project.READ_EXCEL
import org.example.project.START_CRAWL
import org.example.project.UrlsMapResult
import org.example.project.UrlsResult
import org.example.project.downloadZipFile
import org.example.project.getResultSafely
import org.example.project.installContentNegotiation
import org.example.project.module
import org.example.project.openFileSaver
import org.example.project.readUrlsFromExcel
import org.example.project.startCrawling
import org.example.project.uploadExcelAndGetUrls
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.file.Paths
import kotlin.test.Test


class ApplicationTest {
    @Test
    fun testReadExcel() = testApplication {
        application {
            module()
        }
        val testClient = createClient {
            installContentNegotiation()
        }
        // 创建测试用的 Excel 文件
        val excelFile = File(javaClass.classLoader?.getResource("test.xlsx")!!.file).readBytes()
        val response = uploadExcelAndGetUrls(excelFile, READ_EXCEL, testClient)
        assertEquals(HttpStatusCode.OK, response.status)
        val result = response.getResultSafely<UrlsResult>()
        assertTrue(result != null)
        println(result)
    }


    @Test
    fun testStartCrawl() = testApplication {
        application {
            module()
        }
        val testClient = createClient {
            installContentNegotiation()
        }
        val response = startCrawling(
            urls = UrlsResult(listOf("https://item.taobao.com/item.htm?id=903389096131")),
            targetUrl = START_CRAWL,
            myClient = testClient,
        )
        assertEquals(HttpStatusCode.OK, response.status)
        val result = response.getResultSafely<UrlsMapResult>()
        assertTrue(result!= null)
        for ((url, data) in result!!.urlsMap) {
            println("url: $url")
            println("data: $data")
        }
    }

    @Test
    fun testDownloadZip() = testApplication {
        application {
            module()
        }
        val testClient = createClient {
            installContentNegotiation()
        }
        val response = downloadZipFile(
            urls = UrlsResult(listOf("https://img.alicdn.com/imgextra/i3/3522534946/O1CN01xWgp8A1mPJzN39YM0_!!3522534946.jpg_.webp")),
            targetUrl = DOWNLOAD_ZIP,
            myClient = testClient,
        )
        assertEquals(HttpStatusCode.OK, response.status)
        val result = response.getResultSafely<ByteArray>()
        assertTrue(result!= null)
//        openFileSaver(result!!, "test.zip")
    }
}
