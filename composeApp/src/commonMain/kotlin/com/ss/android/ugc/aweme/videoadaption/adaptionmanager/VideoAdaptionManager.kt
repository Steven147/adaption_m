package com.ss.android.ugc.aweme.videoadaption.adaptionmanager

import com.ss.android.ugc.aweme.adaptionmonitor.AbstractAdaptionManager
import com.ss.android.ugc.aweme.adaptionmonitor.AdaptionNodeType
import com.ss.android.ugc.aweme.videoadaption.IAdaptionStrategyFactory
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.VideoAdaptionManagerContext
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.AbstractAdaptionHandler
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionParams
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionResult
import com.ss.android.ugc.aweme.videoadaption.adaptionstrategy.AbstractAdaptionStrategy
import kotlinx.datetime.Clock

/**
 * Created by linshaoqin on 27/3/24
 * @author linshaoqin@bytedance.com
 */
class VideoAdaptionManager(
    override val name: String,
    override val context: VideoAdaptionManagerContext,
) : AbstractAdaptionManager<VideoAdaptionParams, VideoAdaptionResult>(), AdaptionResultInnerReceiver {
    override val nodeType = AdaptionNodeType.MANAGER
    /**
     * 根据传入场景，从策略配置中找到策略对象
     * 调用适配逻辑
     *
     * result null reason:
     * match strategy is null
     *   @see [IAdaptionStrategyFactory.getAdaptionStrategy]
     *
     * defaultHandlers is empty
     * async null result
     *   @see [AbstractAdaptionStrategy.doAdaptionStrategy]
     *
     * special params not valid
     *   @see [AbstractAdaptionHandler.checkParamsValid]
     *
     * adaption result null with handler
     *   @see [AbstractAdaptionHandler.handleAdaption]
     */
    override fun doAdaption(params: VideoAdaptionParams) {
        val startTime = Clock.System.now().toEpochMilliseconds()
        // adaption start
        start(params=params) { "[doAdaption] start" }
        val strategy = context.strategyFactory.getAdaptionStrategy(context, params)
        if (strategy == null) {
            onError({ "[doAdaption] match strategy null" }, params)
            return
        }
        strategy.doAdaptionStrategy(params, this@VideoAdaptionManager)

        // adaption end in main
        onDebug({
            val endTime = Clock.System.now().toEpochMilliseconds()
            "[doAdaption] startTime:$startTime, endTime in main:$endTime, duration:${endTime - startTime}"
        })
    }

    override fun onAdaptionResult(result: VideoAdaptionResult?) {
        result.saveAdaptionResult()
        result.noticeListeners()
        end(result=result) { "[doAdaption->onAdaptionResult]" }
    }
}