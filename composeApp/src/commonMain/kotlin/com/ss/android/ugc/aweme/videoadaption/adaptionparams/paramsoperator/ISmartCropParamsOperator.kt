package com.ss.android.ugc.aweme.videoadaption.adaptionparams.paramsoperator

import com.ss.android.ugc.aweme.videoadaption.adaptionparams.smartparams.ObstructedAreaRegularRect
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.smartparams.RelativeRect

//
interface ISmartCropParamsOperator: IVideoAdaptionParamsOperator {
    val effectiveStickerAreas: List<RelativeRect>?
        get() = null // todo change to area rect
    val obstructedInteractAreas: List<ObstructedAreaRegularRect>?
        get() = null
}

//
interface IDowngradeParamsOperator: IVideoAdaptionParamsOperator {
    val enableSmartCrop: Boolean
        get() = false
    val enableMultiContainer: Boolean
        get() = false
}
//
interface IContainerThresholdParamsOperator: IVideoAdaptionParamsOperator {
    val containerThresholds: List<Float>?
}
