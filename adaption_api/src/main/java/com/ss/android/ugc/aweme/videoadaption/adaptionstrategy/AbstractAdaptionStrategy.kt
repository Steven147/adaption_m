package com.ss.android.ugc.aweme.videoadaption.adaptionstrategy

import com.ss.android.ugc.aweme.adaptionmonitor.AdaptionNodeType
import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionNode
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.IAdaptionStrategyContext
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.AbstractAdaptionHandler
import com.ss.android.ugc.aweme.videoadaption.adaptionmanager.AdaptionResultInnerReceiver
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionParams
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 适配策略接口
 *
 */
abstract class AbstractAdaptionStrategy : IAdaptionNode {
    override val nodeType = AdaptionNodeType.STRATEGY
    abstract val context: IAdaptionStrategyContext

    /**
     * 配置是否异步执行策略
     */
    open val needAsyncCall: Boolean = false

    /**
     * 判断策略是否匹配，用于工厂生产strategy策略
     */
    abstract fun matchStrategy(params: VideoAdaptionParams): Boolean

    /**
     * 实现本策略下的适配
     *
     * @param params VideoAdaptionParams
     * @return VideoAdaptionResult?
     */
    open fun doAdaptionStrategy(params: VideoAdaptionParams, callback: AdaptionResultInnerReceiver) {
        val handlers = context.handlerFactory.getAdaptionHandlers(params, context)
        if (handlers.isEmpty()) {
            onError({ "[doAdaptionStrategy] defaultHandlers is empty" })
            return
        }
        val result: VideoAdaptionResult?
        when {
            needAsyncCall && handlers.size > 1 -> {
                onInfo({ "[doAdaptionStrategy] asyncFromSecond handler ${handlers.map { it.name }}" })
                val firstHandler = listOf(handlers[0])
                val remainHandlers = mutableListOf(handlers).removeAt(0)
                // save and return first result, update further result later
                result = processHandlers(firstHandler, params)
                // use first result for further handler
                processHandlersInBackground(remainHandlers, params, callback, result?.copy())
            }

            else -> { // case: not need async call  OR  handler size == 1
                onDebug({ "[doAdaptionStrategy] mainForAll handler ${handlers.map { it.name }}" })
                result = processHandlers(handlers, params)
            }
        }
        callback.onAdaptionResult(result)
    }

    private var scope = CoroutineScope(Dispatchers.IO)
    private var mainScope = CoroutineScope(Dispatchers.Main)

    private fun processHandlersInBackground(
        handlers: List<AbstractAdaptionHandler>,
        params: VideoAdaptionParams,
        callback: AdaptionResultInnerReceiver,
        oldResult: VideoAdaptionResult? = null,
    ) {
        scope.launch {
            val asyncResult = processHandlers(handlers, params, oldResult)
            onInfo({ "[doAdaptionStrategy] async result" }, result = asyncResult)
            mainScope.launch {
                callback.onAdaptionResult(asyncResult)
            }
        }
    }

    private fun processHandlers(
        handlers: List<AbstractAdaptionHandler>,
        params: VideoAdaptionParams,
        oldResult: VideoAdaptionResult? = null,
    ): VideoAdaptionResult? {
        var result: VideoAdaptionResult? = oldResult
        for (handler in handlers) { // do adaption for all handlers
            onDebug({ "[processHandlers] handler:${handler.name}" }, params, result)
//            if (!handler.checkParamsValid(result, params)) {
//                onInfo({ "[processHandlers] params / old result not valid" }, result = result)
//                return result
//            }
            val nextResult = handler.handleAdaption(result, params)
            if (result == null && nextResult == null) {
                onError({ "[processHandlers] adaption result null" }, params)
                return null
            } else if (result != null && nextResult == null) {
                onInfo({ "[processHandlers] next adaption not valid" }, result = result)
                return result
            }
            result = nextResult
            onDebug({ "[processHandlers]" }, result = result)
        }
        return result
    }
}

