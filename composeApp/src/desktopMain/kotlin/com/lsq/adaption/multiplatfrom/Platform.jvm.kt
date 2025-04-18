package com.lsq.adaption.multiplatfrom

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun getIOScope(): CoroutineScope {
    return CoroutineScope(Dispatchers.IO)
}