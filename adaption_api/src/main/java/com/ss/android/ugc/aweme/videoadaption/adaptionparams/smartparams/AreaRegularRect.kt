package com.ss.android.ugc.aweme.videoadaption.adaptionparams.smartparams

import kotlin.math.abs

/**
 * Created by linshaoqin on 2024/3/21
 * @author linshaoqin@bytedance.com
 */
// rect abstract class of video sticker area & occlusion area
abstract class AreaRegularRect(
    val tag: String,
) {

    fun isValid() = !(getRectRegularLeft() equalsWithEpsilon getRectRegularRight())
        && !(getRectRegularTop() equalsWithEpsilon getRectRegularBottom()) // width & height not equal 0

    // get rect left in container
    abstract fun getRectRegularLeft(): Double

    // get rect end in container
    abstract fun getRectRegularRight(): Double

    // get rect top in container
    abstract fun getRectRegularTop(): Double

    // get rect bottom in container
    abstract fun getRectRegularBottom(): Double
}

// equal of double
infix fun Double.equalsWithEpsilon(other: Double): Boolean {
    val epsilon = 0.00001
    return abs(this - other) < epsilon
}