package com.ss.android.ugc.aweme.videoadaption.adaptioncontext

import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionContext
import com.ss.android.ugc.aweme.videoadaption.IAdaptionHandlerFactory
import com.ss.android.ugc.aweme.videoadaption.IAdaptionStrategyFactory

/**
 * Created by linshaoqin on 10/4/24
 * @author linshaoqin@bytedance.com
 */
// video manager layer
interface IAdaptionManagerContext: IAdaptionContext {
    val strategyFactory: IAdaptionStrategyFactory
}

// strategy layer, obtain manager context
interface IAdaptionStrategyContext: IAdaptionContext {
    val handlerFactory: IAdaptionHandlerFactory
    val managerContext: IAdaptionManagerContext
}

// handler layer, obtain strategy context
interface IAdaptionHandlerContext: IAdaptionContext {
    val strategyContext: IAdaptionStrategyContext
}