package com.ss.android.ugc.aweme.adaptionmonitor

/**
 * Created by linshaoqin on 28/3/24
 * @author linshaoqin@bytedance.com
 *
 * 适配节点：manager对象 / strategy对象 / handler对象
 * 拥有数据/异常上报能力
 */
interface IAdaptionNode {
    val nodeType: AdaptionNodeType
    val name: String

    /**
     * @param params VideoAdaptionParams
     * @param result VideoAdaptionResult
     */
    fun onDebug(message: ()-> String, params: IAdaptionParams? = null, result: IAdaptionResult? = null) {
        AdaptionMonitor.updateNode(
            AdaptionState.DEBUG,
            nodeType,
            name,
            message = message,
            params = params,
            result = result,
        )
    }

    /**
     * @param params VideoAdaptionParams
     * @param result VideoAdaptionResult
     */
    fun onInfo(message: ()-> String, params: IAdaptionParams? = null, result: IAdaptionResult? = null) {
        AdaptionMonitor.updateNode(
            AdaptionState.INFO,
            nodeType,
            name,
            message = message,
            params = params,
            result = result,
        )
    }

    /**
     * @param params VideoAdaptionParams
     * @param result VideoAdaptionResult
     */
    fun onError(message: ()-> String, params: IAdaptionParams? = null, result: IAdaptionResult? = null) {
        AdaptionMonitor.updateNode(
            AdaptionState.ERROR,
            nodeType,
            name,
            message = message,
            params = params,
            result = result,
        )
    }

    //
    fun fail(
        context: IAdaptionContext? = null,
        params: IAdaptionParams? = null,
        result: IAdaptionResult? = null,
        message: MessageGetter
    ) {
        AdaptionMonitor.fail(
            "type" to { nodeType },
            "name" to { name },
            "msg" to message,
            context?.let { "context" to { it } },
            params?.let { "params" to { it } },
            result?.let { "result" to { it } },
        )
    }

    // finish branch, not end process
    fun finish(
        context: IAdaptionContext? = null,
        params: IAdaptionParams? = null,
        result: IAdaptionResult? = null,
        message: MessageGetter
    ) {
        AdaptionMonitor.finish(
            "type" to { nodeType },
            "name" to { name },
            "msg" to message,
            context?.let { "context" to { it } },
            params?.let { "params" to { it } },
            result?.let { "result" to { it } },
        )
    }
    // end process
    fun end(
        context: IAdaptionContext? = null,
        params: IAdaptionParams? = null,
        result: IAdaptionResult? = null,
        message: MessageGetter
    ) {
        AdaptionMonitor.end(
            "type" to { nodeType },
            "name" to { name },
            "msg" to message,
            context?.let { "context" to { it } },
            params?.let { "params" to { it } },
            result?.let { "result" to { it } },
        )
    }

    // star process by params
    fun start(
        context: IAdaptionContext? = null,
        params: IAdaptionParams? = null,
        result: IAdaptionResult? = null,
        message: MessageGetter
    ) {
        AdaptionMonitor.start(
            "type" to { nodeType },
            "name" to { name },
            "msg" to message,
            context?.let { "context" to { it } },
            params?.let { "params" to { it } },
            result?.let { "result" to { it } },
        )
    }
    // continue process by params
    fun continueBy(
        context: IAdaptionContext? = null,
        params: IAdaptionParams? = null,
        result: IAdaptionResult? = null,
        message: MessageGetter
    ) {
        AdaptionMonitor.continueBy(
            "type" to { nodeType },
            "name" to { name },
            "msg" to message,
            context?.let { "context" to { it } },
            params?.let { "params" to { it } },
            result?.let { "result" to { it } },
        )
    }
}