package com.lsq.adaption.multiplatfrom

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.openFileSaver
import io.github.vinceglb.filekit.write
import io.github.vinceglb.filekit.writeString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.example.project.IOSPlatform
import platform.UIKit.UIDevice

class NativePlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = NativePlatform()

actual fun getIOScope(): CoroutineScope {
    return CoroutineScope(Dispatchers.Default)
}