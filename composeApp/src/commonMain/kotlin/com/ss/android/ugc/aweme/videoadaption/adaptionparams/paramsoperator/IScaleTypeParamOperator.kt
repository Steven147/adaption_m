package com.ss.android.ugc.aweme.videoadaption.adaptionparams.paramsoperator

import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.AdaptionScaleType


// when further handler result need to change threshold (scaleType), use [IScaleTypeResultOperator]
interface IScaleTypeParamOperator: IVideoAdaptionParamsOperator {
    // when set fit or crop, will use default strategy and force scale (no edge and translate)
    val forceScaleType: AdaptionScaleType?
}