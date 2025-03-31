package com.ss.android.ugc.aweme.videoadaption.adaptioncontext

import com.ss.android.ugc.aweme.videoadaption.IAdaptionHandlerFactory
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.IFeedScreenContext

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


class DefaultVideoAdaptionStrategyContext(
//    val enableAsyncAdaption: Boolean,
    @Transient override val managerContext: VideoAdaptionManagerContext,
    @Transient override val handlerFactory: IAdaptionHandlerFactory
) : IAdaptionStrategyContext

// smart crop handler context, save params relate to feed smart crop handler
class FeedSmartCropAdaptionHandlerContext(
    val recordMobOnly: Boolean,
    val moveDownAvoidBlackBar: Boolean,
    val shrinkForOut: Boolean,
    val shrinkForText: Boolean,
    override val strategyContext: DefaultVideoAdaptionStrategyContext
) : IAdaptionHandlerContext

// threshold handler context, save params relate to threshold handler
class ThresholdAdaptionHandlerContext(
    override val widthThreshold: Float,
    override val heightThreshold: Float,
    override val strategyContext: IAdaptionStrategyContext
) : IAdaptionHandlerContext, IThresholdContext



//
data class FeedMultiContainerThresholdStrategyContext(
    override val feedScreenContext: IFeedScreenContext?,
    @Transient override val handlerFactory: IAdaptionHandlerFactory,
    @Transient override val managerContext: IAdaptionManagerContext
): IAdaptionStrategyContext, IAdaptionScreenContext


//
data class MultiContainerThresholdHandlerContext(
    override val widthThresholds: List<Float>,
    override val heightThresholds: List<Float>,
    @Transient override val strategyContext: IAdaptionStrategyContext,
) : IAdaptionHandlerContext, IThresholdListContext