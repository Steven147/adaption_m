package com.ss.android.ugc.aweme.videoadaption.adaptionparams.resultoperator

import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.AdaptionScaleType
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.AlignType
import kotlinx.serialization.Serializable

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
@Serializable
data class ThresholdAdaptionResultOperator(
    override val adaptionScaleType: AdaptionScaleType,
    override val alignType: AlignType,
    override val areaDiff: Float,
): IThresholdAdaptionResultOperator