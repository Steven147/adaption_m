package org.example.project

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.cio.Response
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect suspend fun openFileSaver(bytes: ByteArray, fileName: String)

expect suspend fun getUrlsResult(excelFile: ByteArray): UrlsResult

expect suspend fun getUrlsMapResult(urlsResult: UrlsResult): UrlsMapResult

expect suspend fun getBytes(urlsResult: UrlsResult): ByteArray

/**
    graph TD
    A[上传Excel] --> B[显示URL列表]
    B --> C{用户操作}
    C -->|查看详情| D[显示爬取结果]
    C -->|下载打包| E[获取ZIP文件]
 */

// install for client or test client
fun HttpClientConfig<*>.installContentNegotiation() {
    install(ContentNegotiation) { json() }
}

suspend fun uploadExcelAndGetUrls(
    excelFile: ByteArray,
    targetUrl: String = "http://$SERVER_ADDRESS:$SERVER_PORT$READ_EXCEL",
    myClient: HttpClient = HttpClient {
        installContentNegotiation()
    },
): HttpResponse {
    return getResponse(targetUrl, myClient) {
        setBody(MultiPartFormDataContent(formData {
            append("excelFile", excelFile, Headers.build {
                append(HttpHeaders.ContentType, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                // content disposition for file upload [PartData.FileItem]
                append(HttpHeaders.ContentDisposition, "form-data; name=\"excelFile\"; filename=\"upload.xlsx\"")
            })
        }))
    }
}

suspend fun startCrawling(
    urls: UrlsResult,
    targetUrl: String = "http://$SERVER_ADDRESS:$SERVER_PORT$START_CRAWL",
    myClient: HttpClient = HttpClient {
        installContentNegotiation()
    },
): HttpResponse {
    return getResponse(targetUrl, myClient) {
        contentType(ContentType.Application.Json)
        setBody(urls)
    }
}

suspend fun downloadZipFile(
    urls: UrlsResult,
    targetUrl: String = "http://$SERVER_ADDRESS:$SERVER_PORT$DOWNLOAD_ZIP",
    myClient: HttpClient = HttpClient {
        installContentNegotiation()
    },
): HttpResponse {
    return getResponse(targetUrl, myClient) {
        contentType(ContentType.Application.Json)
        setBody(urls)
    }
}


private suspend fun getResponse(
    targetUrl: String,
    myClient: HttpClient,
    block: HttpRequestBuilder.() -> Unit = {}
): HttpResponse = myClient.use {
    it.post(targetUrl, block)
}

suspend inline fun <reified T> HttpResponse.getResultSafely(): T? = this.takeIf { it.status.isSuccess() }?.body()


// 组合使用示例
//suspend fun requestAndSave(scope: CoroutineScope, excelData: ByteArray) {
//    try {
//        // 1. 上传Excel获取URL列表
//        val urlsResult = uploadExcelAndGetUrls(excelData) ?: throw Exception("上传失败")
//
//        // 2. 启动爬取任务
//        val crawlResults = startCrawling(urlsResult) ?: throw Exception("爬取失败")
//        println("获取到 ${crawlResults.urlsMap.size} 条结果")
//
//        // 3. 下载打包文件
//        val zipResult = downloadZipFile(crawlResults.toValues()) ?: throw Exception("下载失败")
//    } catch (e: Exception) {
//        println("处理失败: ${e.message}")
//    }
//}