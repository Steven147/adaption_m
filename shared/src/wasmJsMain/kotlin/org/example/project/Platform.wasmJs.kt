package org.example.project

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.deprecated.openFileSaver

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

actual suspend fun openFileSaver(bytes: ByteArray, fileName: String) {
    FileKit.openFileSaver(bytes = bytes, suggestedName = fileName)
}

actual suspend fun getUrlsResult(excelFile: ByteArray): UrlsResult {
    return uploadExcelAndGetUrls(excelFile).getResultSafely<UrlsResult>() ?: throw Exception("非法结果")
}

actual suspend fun getUrlsMapResult(urlsResult: UrlsResult): UrlsMapResult {
    return startCrawling(urlsResult).getResultSafely<UrlsMapResult>() ?: throw Exception("非法结果")
}

actual suspend fun getBytes(urlsResult: UrlsResult): ByteArray {
    return downloadZipFile(urlsResult).getResultSafely<ByteArray>() ?: throw Exception("非法结果")
}