package com.lsq.adaption.multiplatfrom

import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
actual fun getIOScope(): CoroutineScope {
    return CoroutineScope(Dispatchers.IO)
}