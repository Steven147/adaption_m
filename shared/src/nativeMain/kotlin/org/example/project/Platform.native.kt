package org.example.project

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.write

actual suspend fun openFileSaver(bytes: ByteArray, fileName: String) {
    val file = FileKit.openFileSaver(suggestedName = fileName)
    file?.write(bytes)
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