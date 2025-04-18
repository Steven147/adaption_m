package com.lsq.adaption.multiplatfrom

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()
actual fun getIOScope(): CoroutineScope {
    return CoroutineScope(Dispatchers.Default)
}