package com.ss.android.ugc.aweme.videoadaption

import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.FeedMultiContainerThresholdStrategyContext
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.IAdaptionStrategyContext
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.MultiContainerThresholdHandlerContext
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.ThresholdAdaptionHandlerContext
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.VideoAdaptionManagerContext
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.AbstractAdaptionHandler
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.AdaptionScaleType
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.FeedScreenContext
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.MultiContainerThresholdAdaptionHandler
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.ThresholdAdaptionHandler
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionParams
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.paramsoperator.IContainerThresholdParamsOperator
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.paramsoperator.IScaleTypeParamOperator
import com.ss.android.ugc.aweme.videoadaption.adaptionstrategy.AbstractAdaptionStrategy
import com.ss.android.ugc.aweme.videoadaption.adaptionstrategy.DefaultAdaptionStrategy
import kotlinx.serialization.Serializable

object AdaptionMockDataUtil {
    //
    fun getMockAdaptionManagerContext(
        strategyNameList: List<String> = listOf("default_adaption_strategy"),
        handlerNameList: List<String> = listOf("multi_container_handler"),
        widthThreshold: Float = 0.1f,
        heightThreshold: Float = 0.1f,
        widthThresholds: List<Float> = listOf(0f, 1f, 1f, 1f, 1f, 1f, 1f),
        heightThresholds: List<Float> = listOf(0f, 1f, 1f, 1f, 1f, 1f, 1f),
        feedScreenContext: FeedScreenContext = FeedScreenContext(
            screenWidth = 400f,
            screenHeight = 400 * 20.025f / 9,
            statusBarHeight = 39f,
            topTabHeight = 58f,
            bottomBannerHeight = 48f,
            bottomTabHeight = 49f,
            topTypeList = listOf(0, 1, 3, 0, 1, 3, 3),
            bottomTypeList = listOf(0, 0, 0, 1, 1, 1, 3),
        )
    ): VideoAdaptionManagerContext {
        return VideoAdaptionManagerContext(
            eventType = null,
            strategyFactory = BaseAdaptionStrategyFactory(
                strategyNameList = strategyNameList,
                handlerNameList = handlerNameList,
                widthThreshold = widthThreshold,
                heightThreshold = heightThreshold,
                widthThresholds = widthThresholds,
                heightThresholds = heightThresholds,
                feedScreenContext = feedScreenContext
            )
        )
    }

    fun getMockAdaptionParamsOperator(
        forceScaleType: AdaptionScaleType? = null,
        containerThresholds: List<Float>? = listOf(0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f)
    ): BaseAdaptionParamOperator {
        return BaseAdaptionParamOperator(
            forceScaleType = forceScaleType,
            containerThresholds = containerThresholds
        )
    }
}

//

@Serializable
data class BaseAdaptionStrategyFactory(
    val strategyNameList: List<String>,
    val handlerNameList: List<String>,
    val widthThreshold: Float,
    val heightThreshold: Float,
    val widthThresholds: List<Float>,
    val heightThresholds: List<Float>,
    val feedScreenContext: FeedScreenContext
): IAdaptionStrategyFactory {
    override fun getAdaptionStrategy(
        context: VideoAdaptionManagerContext,
        params: VideoAdaptionParams
    ): AbstractAdaptionStrategy? {
        return DefaultAdaptionStrategy {
            FeedMultiContainerThresholdStrategyContext(
                feedScreenContext = feedScreenContext,
                handlerFactory = BaseAdaptionHandlerFactory(
                    handlerNameList = handlerNameList,
                    widthThreshold = widthThreshold,
                    heightThreshold = heightThreshold,
                    widthThresholds = widthThresholds,
                    heightThresholds = heightThresholds
                ),
                managerContext = context
            )
        }
    }
}

@Serializable
data class BaseAdaptionHandlerFactory(
    val handlerNameList: List<String>,
    val widthThreshold: Float,
    val heightThreshold: Float,
    val widthThresholds: List<Float>,
    val heightThresholds: List<Float>,
): IAdaptionHandlerFactory {
    override fun getAdaptionHandlers(
        params: VideoAdaptionParams,
        context: IAdaptionStrategyContext
    ): List<AbstractAdaptionHandler> {
        return listOf(
            MultiContainerThresholdAdaptionHandler.getOrCreate(
                MultiContainerThresholdHandlerContext(
                    widthThresholds = widthThresholds,
                    heightThresholds = heightThresholds,
                    strategyContext = context
                )
            ),
            ThresholdAdaptionHandler.getOrCreate(
                ThresholdAdaptionHandlerContext(
                    widthThreshold = widthThreshold,
                    heightThreshold = heightThreshold,
                    strategyContext = context
                )
            )
        ).filter { it.name in handlerNameList }
    }
}


@Serializable
data class BaseAdaptionParamOperator(
    override val forceScaleType: AdaptionScaleType?,
    override val containerThresholds: List<Float>?
): IScaleTypeParamOperator, IContainerThresholdParamsOperator

