package com.ss.android.ugc.aweme.videoadaption.adaptionparams.paramsoperator

import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionParams
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionResult

/**
 * Created by linshaoqin on 24/4/24
 * @author linshaoqin@bytedance.com
 */
data class VideoProgressAdaptionParams(
    val initResult: VideoAdaptionResult,
    val finalResult: VideoAdaptionResult?,
) : IAdaptionParams
