package com.lsq.adaption.multiplatfrom

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.deprecated.openFileSaver
import io.github.vinceglb.filekit.download
import io.github.vinceglb.filekit.utils.toJsArray
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.url.URL
import org.w3c.files.File

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()
actual fun getIOScope(): CoroutineScope {
    return CoroutineScope(Dispatchers.Default)
}
