package com.ss.android.ugc.aweme.videoadaption.adaptionparams

import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionResult
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.resultoperator.IVideoAdaptionResultOperator
import kotlinx.serialization.Serializable

/**
 * Created by linshaoqin on 2024/3/21
 * @author linshaoqin@bytedance.com
 */
// adaption result
@Serializable
data class VideoAdaptionResult(
    val width: Int,
    val height: Int,
    val translateX: Float? = null,
    val translateY: Float? = null,
    val resultOperator: IVideoAdaptionResultOperator? = null
) : IAdaptionResult {
    // check valid
    fun isDimensionValid() = width > 0 && height > 0

}

