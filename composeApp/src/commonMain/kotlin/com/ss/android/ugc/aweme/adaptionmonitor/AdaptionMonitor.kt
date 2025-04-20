package com.ss.android.ugc.aweme.adaptionmonitor

/**
 * Created by linshaoqin on 28/3/24
 * @author linshaoqin@bytedance.com
 */
object AdaptionMonitor: BaseMonitor() {
    private const val TAG = "AdaptionMonitor"
    private var logEnable = true

    override val enableALog = false
    override val enableApmLog = false
    override val enableLogcat = true

    //
    fun initMonitorContext(logEnable: Boolean) {
        this.logEnable = logEnable
    }

    /**
     * @param state AdaptionState
     * @param node VideoAdaptionNode
     * @param name String
     * @param message String
     *  todo add slardar / mob report with BaseMonitor
     */
    fun updateNode(
        state: AdaptionState, node: AdaptionNodeType, name: String, message: ()-> String,
        params: IAdaptionParams?, result: IAdaptionResult?
    ) {
        if (!logEnable) return
        val data = "message:${message.invoke()}, params:$params, result:$result"
        when (state) {
            AdaptionState.ERROR -> {
//                Log.e(TAG, "nodeType:$node, name:$name, $data")
            }

            AdaptionState.INFO -> {
//                Log.i(TAG, "nodeType:$node, name:$name, $data")
            }

            AdaptionState.DEBUG -> {
//                Log.d(TAG, "nodeType:$node, name:$name, $data")
            }
        }
    }

}

enum class AdaptionNodeType(val string: String) {
    MANAGER("manager"), STRATEGY("strategy"), HANDLER("handler"), BUSINESS("Business")
}

enum class AdaptionState {
    ERROR, INFO, DEBUG
}