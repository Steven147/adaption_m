package com.ss.android.ugc.aweme.videoadaption

import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.VideoAdaptionManagerContext
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionParams
import com.ss.android.ugc.aweme.videoadaption.adaptionstrategy.AbstractAdaptionStrategy

/**
 * Created by linshaoqin on 26/4/24
 * @author linshaoqin@bytedance.com
 */
interface IAdaptionStrategyFactory {
    /**
     * get target adaption strategy from nominee, strategy will be triggered by manager
     */
    fun getAdaptionStrategy(context: VideoAdaptionManagerContext, params: VideoAdaptionParams):
        AbstractAdaptionStrategy?
}