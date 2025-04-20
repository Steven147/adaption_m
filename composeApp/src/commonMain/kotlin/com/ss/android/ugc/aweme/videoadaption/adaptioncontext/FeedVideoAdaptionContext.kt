package com.ss.android.ugc.aweme.videoadaption.adaptioncontext

import com.ss.android.ugc.aweme.videoadaption.IAdaptionHandlerFactory
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.IFeedScreenContext
import kotlinx.serialization.Serializable

/**
 * Created by linshaoqin on 10/4/24
 * @author linshaoqin@bytedance.com
 */

//
interface IAdaptionScreenContext {
    val feedScreenContext: IFeedScreenContext?
}
//
interface IThresholdListContext {
    val widthThresholds: List<Float>
    val heightThresholds: List<Float>
}

//
interface IThresholdContext {
    val widthThreshold: Float
    val heightThreshold: Float
}

//
interface ISmartCropContext {
    val recordMobOnly: Boolean
    val moveDownAvoidBlackBar: Boolean
    val shrinkForOut: Boolean
    val shrinkForText: Boolean
}

@Serializable
class DefaultVideoAdaptionStrategyContext(
//    val enableAsyncAdaption: Boolean,
    override val managerContext: VideoAdaptionManagerContext,
    override val handlerFactory: IAdaptionHandlerFactory
) : IAdaptionStrategyContext

// smart crop handler context, save params relate to feed smart crop handler
@Serializable
class FeedSmartCropAdaptionHandlerContext(
    val recordMobOnly: Boolean,
    val moveDownAvoidBlackBar: Boolean,
    val shrinkForOut: Boolean,
    val shrinkForText: Boolean,
    override val strategyContext: DefaultVideoAdaptionStrategyContext
) : IAdaptionHandlerContext

// threshold handler context, save params relate to threshold handler
@Serializable
class ThresholdAdaptionHandlerContext(
    override val widthThreshold: Float,
    override val heightThreshold: Float,
    override val strategyContext: IAdaptionStrategyContext
) : IAdaptionHandlerContext, IThresholdContext



//
@Serializable
data class FeedMultiContainerThresholdStrategyContext(
    override val feedScreenContext: IFeedScreenContext?,
    override val handlerFactory: IAdaptionHandlerFactory,
    override val managerContext: IAdaptionManagerContext
): IAdaptionStrategyContext, IAdaptionScreenContext


//
@Serializable
data class MultiContainerThresholdHandlerContext(
    override val widthThresholds: List<Float>,
    override val heightThresholds: List<Float>,
    override val strategyContext: IAdaptionStrategyContext,
) : IAdaptionHandlerContext, IThresholdListContext