package com.ss.android.ugc.aweme.videoadaption.adaptionparams.paramsoperator

import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionParams
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionResult
import kotlinx.serialization.Serializable

/**
 * Created by linshaoqin on 24/4/24
 * @author linshaoqin@bytedance.com
 */
@Serializable
data class VideoProgressAdaptionParams(
    val initResult: VideoAdaptionResult,
    val finalResult: VideoAdaptionResult?,
) : IAdaptionParams
