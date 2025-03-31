package com.ss.android.ugc.aweme.videoadaption.adaptionparams

import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionContext
import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionParams
import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionResult
import java.io.Serializable

/**
 * @author linshaoqin
 *
 * save adaption params, context and result
 */
data class VideoAdaptionDetail(
    var context: IAdaptionContext? = null,
    var params: IAdaptionParams? = null,
    var result: IAdaptionResult? = null,
): Serializable