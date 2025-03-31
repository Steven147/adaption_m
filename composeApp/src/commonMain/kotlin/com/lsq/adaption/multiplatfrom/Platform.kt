package com.lsq.adaption.multiplatfrom

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform