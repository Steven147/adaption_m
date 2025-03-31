package com.ss.android.ugc.aweme.videoadaption.adaptionparams.resultoperator

import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionResult

/**
 * Created by linshaoqin on 2024/3/27
 * @author linshaoqin@bytedance.com
 */
interface AdaptionResultListener<T : IAdaptionResult> {

    //
    fun onAdaptionChanged(result: T?)
}