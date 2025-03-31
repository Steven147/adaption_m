package com.ss.android.ugc.aweme.screenadaption.adaptionmanager

import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionContext

/**
 * Created by linshaoqin on 29/4/24
 * @author linshaoqin@bytedance.com
 */
class ScreenAdaptionContext(
    val context: Any,
    val forceOpenTop: Boolean? = null,
    val forceOpenBottom: Boolean? = null,
) : IAdaptionContext