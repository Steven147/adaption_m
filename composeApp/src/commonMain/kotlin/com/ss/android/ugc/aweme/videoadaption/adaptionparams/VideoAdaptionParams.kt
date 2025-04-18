package com.ss.android.ugc.aweme.videoadaption.adaptionparams

import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionParams
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.paramsoperator.IVideoAdaptionParamsOperator
import kotlinx.serialization.Serializable

/**
 * Created by linshaoqin on 16/3/24
 * @author linshaoqin@bytedance.com
 */
// adaption common params, changed by video
// context / params / operator
// params 扩展后，框架不变
@Serializable
data class VideoAdaptionParams(
    val containerWidth: Int,
    val containerHeight: Int,
    val videoWidth: Int,
    val videoHeight: Int,
    val paramsOperator: IVideoAdaptionParamsOperator? = null
) : IAdaptionParams {
    val videoRatio = videoHeight.toFloat() / videoWidth
}

// check valid
fun VideoAdaptionParams.isDimensionValid() =
    this.containerWidth > 0 && this.containerHeight > 0 && this.videoWidth > 0 && this.videoHeight > 0