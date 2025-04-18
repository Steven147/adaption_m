package com.ss.android.ugc.aweme.videoadaption.adaptionparams.resultoperator

import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.AdaptionScaleType
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.AlignType

/**
 * @author linshaoqin
 *
 * operator of adaption result, save threshold special result
 */
interface IThresholdAdaptionResultOperator: IVideoAdaptionResultOperator {
    val adaptionScaleType: AdaptionScaleType
    val alignType: AlignType
    val areaDiff: Float
}

/**
 *
 */
data class ThresholdAdaptionResultOperator(
    override val adaptionScaleType: AdaptionScaleType,
    override val alignType: AlignType,
    override val areaDiff: Float,
): IThresholdAdaptionResultOperator