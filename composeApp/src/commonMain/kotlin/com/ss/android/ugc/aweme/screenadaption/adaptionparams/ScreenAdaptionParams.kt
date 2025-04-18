package com.ss.android.ugc.aweme.screenadaption.adaptionparams

import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionParams

/**
 * Created by linshaoqin on 28/3/24
 * @author linshaoqin@bytedance.com
 */
data class ScreenAdaptionParams(
    val appNavHeight: Int,
    val activity: Any? = null,
    val forceOpenTop: Boolean? = null,
    val forceOpenBottom: Boolean? = null,
) : IAdaptionParams
