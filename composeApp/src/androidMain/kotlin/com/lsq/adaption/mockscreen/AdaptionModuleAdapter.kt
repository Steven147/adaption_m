package com.lsq.adaption.mockscreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.AdaptionScaleType
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.AdaptionPaddingValues

/**
 * Created by linshaoqin on 24/07/24
 * @author linshaoqin@bytedance.com
 */
object AdaptionModuleAdapter {
}

// change adaption Scale Type to content scale
fun AdaptionScaleType.toContentScale(): ContentScale? = when (this) {
    AdaptionScaleType.FIT -> ContentScale.Fit
    AdaptionScaleType.CROP -> ContentScale.Crop
//    AdaptionScaleType.BOTH -> null
//    AdaptionScaleType.NONE -> ContentScale.None
//    AdaptionScaleType.FILL_BOUNDS -> ContentScale.FillBounds
}

//
fun ContentScale.toAdaptionScaleType(): AdaptionScaleType? = when (this) {
    ContentScale.Fit -> AdaptionScaleType.FIT
    ContentScale.Crop -> AdaptionScaleType.CROP
    else -> null
}

//
fun AdaptionPaddingValues.toPaddingValues(scale: Float) = PaddingValues(
    left.times(scale).dp,
    top.times(scale).dp,
    right.times(scale).dp,
    bottom.times(scale).dp
)
