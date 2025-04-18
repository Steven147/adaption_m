package com.ss.android.ugc.aweme.videoadaption

import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.IAdaptionStrategyContext
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.AbstractAdaptionHandler
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionParams

/**
 * Created by linshaoqin on 28/4/24
 * @author linshaoqin@bytedance.com
 */
interface IAdaptionHandlerFactory {
    /**
     * get adaption handler list, strategy will trigger in order
     */
    fun getAdaptionHandlers(
        params: VideoAdaptionParams,
        context: IAdaptionStrategyContext
    ): List<AbstractAdaptionHandler>
}