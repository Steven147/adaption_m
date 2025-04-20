package com.ss.android.ugc.aweme.videoadaption.adaptionparams.smartparams

/**
 * Created by linshaoqin on 22/4/24
 * @author linshaoqin@bytedance.com
 */

// update regular set overlap
fun updateOverlap(
    effectiveAreaSet: EffectiveAreaRegularSet,
    obstructedAreaSet: ObstructedAreaRegularSet,
    callback: (EffectiveAreaRegularRect, ObstructedAreaRegularRect) -> Unit,
): Boolean {
    var hasOverlap = false
    obstructedAreaSet.areaRects.onEach { obstructedAreaRect ->
        effectiveAreaSet.areaRects.onEach { effectiveAreaRect ->
            if (hasOverlap(effectiveAreaRect, obstructedAreaRect)) {
                hasOverlap = true
                callback.invoke(effectiveAreaRect, obstructedAreaRect)
            }
        }
    }
    return hasOverlap
}


// return if two area has overlap
fun hasOverlap(
    effectiveAreaRect: EffectiveAreaRegularRect,
    obstructedAreaRect: ObstructedAreaRegularRect
): Boolean {
    return calOverlap(effectiveAreaRect, obstructedAreaRect) > 0.0
}

// return two area overlap area size
fun calOverlap(
    effectiveAreaRect: EffectiveAreaRegularRect,
    obstructedAreaRect: ObstructedAreaRegularRect
): Double {
    val leftOverlap = maxOf(effectiveAreaRect.getRectRegularLeft(), obstructedAreaRect.getRectRegularLeft())
    val topOverlap = maxOf(effectiveAreaRect.getRectRegularTop(), obstructedAreaRect.getRectRegularTop())
    val rightOverlap = minOf(effectiveAreaRect.getRectRegularRight(), obstructedAreaRect.getRectRegularRight())
    val bottomOverlap = minOf(effectiveAreaRect.getRectRegularBottom(), obstructedAreaRect.getRectRegularBottom())

    val widthOverlap = maxOf(0.0, rightOverlap - leftOverlap)
    val heightOverlap = maxOf(0.0, bottomOverlap - topOverlap)
    var result = widthOverlap * heightOverlap
    if (result equalsWithEpsilon 0.0) result = 0.0
    return result
}