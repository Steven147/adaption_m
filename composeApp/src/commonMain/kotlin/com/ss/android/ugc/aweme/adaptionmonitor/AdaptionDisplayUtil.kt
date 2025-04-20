package com.ss.android.ugc.aweme.adaptionmonitor

import kotlin.math.roundToInt

object AdaptionDisplayUtil {

    //
    fun Float.round1() = (this * 10).roundToInt() / 10.0f

    //
    fun Float.round2() = (this * 100).roundToInt() / 100.0f

    //
    fun Float.round3() = (this * 1000).roundToInt() / 1000.0f

    //
    fun Float.getScaleBy9(): String =
        if (this >= 1) "${this.round2() * 9}/9"
        else "9/${9 / (this.round2())}"
}

