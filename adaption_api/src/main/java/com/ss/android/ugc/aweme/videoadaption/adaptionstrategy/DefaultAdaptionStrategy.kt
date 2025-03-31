package com.ss.android.ugc.aweme.videoadaption.adaptionstrategy

import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.DefaultVideoAdaptionStrategyContext
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.IAdaptionStrategyContext
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionParams

/**
 * Created by linshaoqin on 26/3/24
 * @author linshaoqin@bytedance.com
 */
class DefaultAdaptionStrategy(
    private val contextGetter: () -> IAdaptionStrategyContext,
) : AbstractAdaptionStrategy() {
    override val context by lazy { contextGetter.invoke() }
    override val name: String = "default_adaption_strategy"
    override fun matchStrategy(params: VideoAdaptionParams): Boolean {
        return true
    }
}