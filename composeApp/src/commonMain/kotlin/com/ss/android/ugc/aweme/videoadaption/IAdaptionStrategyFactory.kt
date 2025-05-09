package com.ss.android.ugc.aweme.videoadaption

import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.VideoAdaptionManagerContext
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionParams
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.paramsoperator.IVideoAdaptionParamsOperator
import com.ss.android.ugc.aweme.videoadaption.adaptionstrategy.AbstractAdaptionStrategy
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

/**
 * Created by linshaoqin on 26/4/24
 * @author linshaoqin@bytedance.com
 */
interface IAdaptionStrategyFactory {
    /**
     * get target adaption strategy from nominee, strategy will be triggered by manager
     */
    fun getAdaptionStrategy(context: VideoAdaptionManagerContext, params: VideoAdaptionParams):
        AbstractAdaptionStrategy?
}

// 创建 SerializersModule 并注册序列化器
val module = SerializersModule {
    polymorphic(IAdaptionStrategyFactory::class) {
        subclass(BaseAdaptionStrategyFactory::class, BaseAdaptionStrategyFactory.serializer())
    }
    polymorphic(IVideoAdaptionParamsOperator::class) {
        subclass(BaseAdaptionParamOperator::class, BaseAdaptionParamOperator.serializer())
    }
}
