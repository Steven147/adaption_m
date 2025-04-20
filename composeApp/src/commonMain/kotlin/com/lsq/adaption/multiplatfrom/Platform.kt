package com.lsq.adaption.multiplatfrom

import kotlinx.coroutines.CoroutineScope

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform


expect fun getIOScope(): CoroutineScope
