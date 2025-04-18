package com.ss.android.ugc.aweme.videoadaption.adaptioncontext

import com.ss.android.ugc.aweme.videoadaption.IAdaptionStrategyFactory
import kotlinx.serialization.Serializable

/**
 * Created by linshaoqin on 2024/4/10
 * @author linshaoqin@bytedance.com
 */
// video manager context, save common cell manager params that not change by video
@Serializable
data class VideoAdaptionManagerContext(
    val eventType: String?,
    override val strategyFactory: IAdaptionStrategyFactory,
) : IAdaptionManagerContext