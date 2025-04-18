package com.ss.android.ugc.aweme.screenadaption.adaptionparams

import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionParams
import kotlinx.serialization.Serializable

/**
 * Created by linshaoqin on 28/3/24
 * @author linshaoqin@bytedance.com
 */
@Serializable
data class ScreenAdaptionParams(
    val appNavHeight: Int,
    val forceOpenTop: Boolean? = null,
    val forceOpenBottom: Boolean? = null,
) : IAdaptionParams
