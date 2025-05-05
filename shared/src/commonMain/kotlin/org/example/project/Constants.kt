package org.example.project

const val SERVER_PORT = 5000

const val SERVER_ADDRESS = "127.0.0.1"

const val READ_EXCEL = "/read-excel"
const val START_CRAWL = "/start-crawl"
const val DOWNLOAD_ZIP = "/download-zip"

// 新增数据类
@kotlinx.serialization.Serializable
data class UrlsResult(
    val urls: List<String>
)

@kotlinx.serialization.Serializable
data class UrlsMapResult(
    val urlsMap: Map<String, String>
) {
    fun toValues(): UrlsResult {
        return UrlsResult(urlsMap.values.toList())
    }
}

// todo use proto buf