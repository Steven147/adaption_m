package com.ss.android.ugc.aweme.videoadaption.adaptionmanager

import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionResult

/**
 * 适配结果回调
 * 用于同步/异步返回的结果
 */
interface AdaptionResultInnerReceiver {

    /**
     * 适配结果回调函数
     */
    fun onAdaptionResult(result: VideoAdaptionResult?)
}