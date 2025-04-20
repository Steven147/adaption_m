package com.ss.android.ugc.aweme.screenadaption.adaptionmanager

import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionContext
import kotlinx.serialization.Serializable

/**
 * Created by linshaoqin on 29/4/24
 * @author linshaoqin@bytedance.com
 */
@Serializable
class ScreenAdaptionContext(
    val forceOpenTop: Boolean? = null,
    val forceOpenBottom: Boolean? = null,
) : IAdaptionContext