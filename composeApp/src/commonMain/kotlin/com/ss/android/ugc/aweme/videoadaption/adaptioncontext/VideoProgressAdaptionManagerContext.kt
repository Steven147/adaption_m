package com.ss.android.ugc.aweme.videoadaption.adaptioncontext

import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionContext
import kotlinx.serialization.Serializable

/**
 * Created by linshaoqin on 22/4/24
 * @author linshaoqin@bytedance.com
 */
@Serializable
class VideoProgressAdaptionManagerContext(
    val eventType: String?,
) : IAdaptionContext