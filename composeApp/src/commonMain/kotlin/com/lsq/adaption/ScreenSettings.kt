package com.lsq.adaption

import androidx.compose.ui.layout.ContentScale
import com.ss.android.ugc.aweme.videoadaption.AdaptionMockDataUtil.getMockAdaptionManagerContext
import com.ss.android.ugc.aweme.videoadaption.AdaptionMockDataUtil.getMockAdaptionParamsOperator
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.VideoAdaptionManagerContext
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionParams
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionResult

data class ScreenSettings(
    // mock adaption params
    var mockVideoRatioEnable: Boolean = false,
    var mockPaddingEnable: Boolean = false,
    var screenAdaptionTopType: Int = 0,
    var screenAdaptionBottomType: Int = 0,
    // mock adaption scale mode
    var mockScaleEnable: Boolean = false,
    var mockScaleMode: ContentScale = ContentScale.Crop,

    // mock screen params
    var adaptionContext: VideoAdaptionManagerContext = getMockAdaptionManagerContext(),
    var adaptionParams: VideoAdaptionParams =
        VideoAdaptionParams(0, 0,0, 0, getMockAdaptionParamsOperator()),
    var adaptionResult: VideoAdaptionResult =
        VideoAdaptionResult(0, 0),
    // mock screen display params
    var mockScreenScaleValue: Float = 0.70f,
    // mock video params
    var videoRatio: Float = 0.5625f,
)