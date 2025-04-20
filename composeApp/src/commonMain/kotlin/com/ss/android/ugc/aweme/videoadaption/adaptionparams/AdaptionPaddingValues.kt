package com.ss.android.ugc.aweme.videoadaption.adaptionparams

import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionParams
import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionResult
import kotlinx.serialization.Serializable

/**
 * @author linshaoqin
 *
 * save container padding
 * tmp only use in top & bottom
 */
@Serializable
data class AdaptionPaddingValues(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
): IAdaptionResult, IAdaptionParams