package com.ss.android.ugc.aweme.screenadaption.adaptionparams

import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionResult

/**
 * Created by linshaoqin on 28/3/24
 * @author linshaoqin@bytedance.com
 */
data class ScreenAdaptionResult(
    // adaption result value
    val topSpaceHeight: Int, // 0 or statusBarHeight, equal [AdaptationManager.getDesiredTopSpaceHeight()]
    val bottomSpaceHeight: Int, // 0 or appNavHeight
    // static value
    val screenHeight: Int,
    val screenWidth: Int,
    val statusBarHeight: Int,
    val appNavHeight: Int,
    val virtualBarHeight: Int?,
) : IAdaptionResult {
    val containerHeight = screenHeight - topSpaceHeight - bottomSpaceHeight
    val containerWidth: Int = screenWidth
    // 0 means open appNavHeight, 1 means turn black appNavHeight
    @Transient val appNavType: Int = if (bottomSpaceHeight == 0) 0 else 1
    @Transient val statusBarType: Int = if (topSpaceHeight == 0) 0 else 1
    @Transient val screenInsetByTop: Int = statusBarHeight - topSpaceHeight
    @Transient val screenInsetByBottom: Int = appNavHeight - bottomSpaceHeight
    @Transient val showTopSpace: Boolean = topSpaceHeight != 0
    @Transient val showBottomSpace: Boolean = bottomSpaceHeight != 0
}
