package com.ss.android.ugc.aweme.videoadaption.adaptionhandler

import com.ss.android.ugc.aweme.adaptionmonitor.AdaptionNodeType
import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionNode
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.IAdaptionHandlerContext
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionParams
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionResult

/**
 * Created by linshaoqin on 17/3/24
 * @author linshaoqin@bytedance.com
 *
 * 适配处理器抽象接口
 */
abstract class AbstractAdaptionHandler : IAdaptionNode {
    override val nodeType = AdaptionNodeType.HANDLER
    abstract val context: IAdaptionHandlerContext

    /**
     * 校验适配入参是否合法
     *
     * @param oldResult VideoAdaptionResult?
     * @param params VideoAdaptionParams<IAdaptionSpecialParams>
     * @return Boolean?
     */
    @Deprecated("not call", ReplaceWith("true"))
    open fun checkParamsValid(
        oldResult: VideoAdaptionResult?,
        params: VideoAdaptionParams
    ): Boolean = true

    /**
     * 调用处理器适配
     * 【必传】传入适配参数
     * 【可选】传入上一步适配结果
     * 注意：在适配不合法时，若返回空，则默认按照上一步结果直接输出
     *
     * @param oldResult VideoAdaptionResult?
     * @param params VideoAdaptionParams<IAdaptionSpecialParams>
     * @return VideoAdaptionResult?
     */
    abstract fun handleAdaption(
        oldResult: VideoAdaptionResult?,
        params: VideoAdaptionParams
    ): VideoAdaptionResult?
}