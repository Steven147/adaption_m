package com.ss.android.ugc.aweme.videoadaption.adaptionparams.resultoperator

import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.AdaptionScaleType
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.AlignType
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.AdaptionPaddingValues
import kotlinx.serialization.Serializable

/**
 * @author linshaoqin
 *
 * operator of adaption result, save multi container special result
 */
interface IMultiContainerThresholdResultOperator:
    IAdjustContainerResultOperator, IVideoAdaptionResultOperator {
    val topType: Int
    val bottomType: Int
    val topHeight: Float
    val bottomHeight: Float
    //
    val paddingValues: AdaptionPaddingValues
        get() = AdaptionPaddingValues(
            left = 0,
            top = topHeight.toInt(),
            right = 0,
            bottom = bottomHeight.toInt(),
        )
}

//
interface IAdjustContainerResultOperator: IVideoAdaptionResultOperator {
    val adjustContainerHeight: Float
    val adjustContainerWidth: Float
    //
    val adjustContainerRatio: Float
        get() = adjustContainerHeight / adjustContainerWidth
}

//
@Serializable
data class MultiContainerThresholdResultOperator(
    override val topType: Int,
    override val bottomType: Int,
    override val topHeight: Float,
    override val bottomHeight: Float,

    override val adjustContainerHeight: Float,
    override val adjustContainerWidth: Float,

    override val adaptionScaleType: AdaptionScaleType,
    override val alignType: AlignType,
    override val areaDiff: Float,
): IMultiContainerThresholdResultOperator, IThresholdAdaptionResultOperator

//
@Serializable
data class MultiContainerThresholdResultInnerOperator(
    override val topType: Int,
    override val bottomType: Int,
    override val topHeight: Float,
    override val bottomHeight: Float,
    override val adjustContainerHeight: Float,
    override val adjustContainerWidth: Float
): IMultiContainerThresholdResultOperator