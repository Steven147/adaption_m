package com.ss.android.ugc.aweme.adaptionmonitor

import com.ss.android.ugc.aweme.videoadaption.adaptionparams.resultoperator.AdaptionResultListener
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by linshaoqin on 27/3/24
 * @author linshaoqin@bytedance.com
 *
 * 适配manager，每个视频分别持有的适配对象，提供适配能力，提供适配结果感知能力
 */
abstract class AbstractAdaptionManager<P : IAdaptionParams, R : IAdaptionResult>: IAdaptionNode {
    override val nodeType: AdaptionNodeType = AdaptionNodeType.MANAGER

    private val resultListeners: CopyOnWriteArrayList<AdaptionResultListener<R>> = CopyOnWriteArrayList()
    protected var result: R? = null
    abstract val context: IAdaptionContext
    /**
     * 调用适配，返回适配结果
     * 输入基本参数，同时根据适配处理器的需要，传入特殊参数获取回调
     */
    abstract fun doAdaption(params: P)

    /**
     * @return VideoAdaptionResult?
     *
     * 获取适配结果
     */
    fun getAdaptionResult(): R? = result

    /**
     * @return VideoAdaptionResult?
     *
     * 重置适配结果
     */
    fun resetAdaptionResult() {
        result = null
    }

    /**
     * @param listener VideoAdaptionResultListener
     *
     * 监听适配结果变化
     * 输入场景。根据场景监听适配变化
     */
    fun addAdaptionResultListener(listener: AdaptionResultListener<R>, isSticky: Boolean) {
        resultListeners.add(listener)
        if (isSticky) {
            listener.onAdaptionChanged(result)
        }
    }

    /**
     * @param listener VideoAdaptionResultListener
     *
     * 移除监听
     */
    fun removeAdaptionResultListener(listener: AdaptionResultListener<R>) {
        resultListeners.remove(listener)
    }


    protected fun R?.noticeListeners() = this.apply {
        for (listener in resultListeners) {
            listener.onAdaptionChanged(this)
        }
    }

    protected fun R?.saveAdaptionResult() {
        result = this
    }
}