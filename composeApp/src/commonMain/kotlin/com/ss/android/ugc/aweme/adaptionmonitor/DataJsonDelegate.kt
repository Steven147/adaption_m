package com.ss.android.ugc.aweme.adaptionmonitor

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.lsq.adaption.ScreenSettings
import com.ss.android.ugc.aweme.screenadaption.adaptionmanager.ScreenAdaptionContext
import com.ss.android.ugc.aweme.videoadaption.AdaptionMockDataUtil
import com.ss.android.ugc.aweme.videoadaption.BaseAdaptionParamOperator
import com.ss.android.ugc.aweme.videoadaption.BaseAdaptionStrategyFactory
import com.ss.android.ugc.aweme.videoadaption.IAdaptionStrategyFactory
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.VideoAdaptionManagerContext
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionParams
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionResult
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.paramsoperator.IVideoAdaptionParamsOperator
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.resultoperator.IVideoAdaptionResultOperator
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.resultoperator.MultiContainerThresholdResultOperator
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlin.reflect.cast

// 所有具体类型的序列化都需要手动添加
// 手动添加后，需同步添加测试 case
object DataJsonDelegate {
    var isTest = false

    @Deprecated("use delegate instead, public by inline function")
    val localJson = Json {
        prettyPrint = true
        serializersModule = SerializersModule {
            // 创建 SerializersModule 并注册序列化器
            // 如果不是 sealed class，在序列化时，只有接口信息，并没有具体的类信息，也无法转换成具体的类
            // 在此注册接口信息下的具体子类信息，就可以主动转换成具体的类
            polymorphic(IAdaptionStrategyFactory::class) {
                subclass(
                    BaseAdaptionStrategyFactory::class,
                    BaseAdaptionStrategyFactory.serializer()
                )
            }
            polymorphic(IAdaptionData::class) {
                subclass(VideoAdaptionParams::class, VideoAdaptionParams.serializer())
                subclass(VideoAdaptionResult::class, VideoAdaptionResult.serializer())
                subclass(
                    VideoAdaptionManagerContext::class,
                    VideoAdaptionManagerContext.serializer()
                )
                subclass(BaseAdaptionParamOperator::class, BaseAdaptionParamOperator.serializer())
                subclass(ScreenAdaptionContext::class, ScreenAdaptionContext.serializer())
            }
            polymorphic(IVideoAdaptionParamsOperator::class) {
                subclass(BaseAdaptionParamOperator::class, BaseAdaptionParamOperator.serializer())
            }
            polymorphic(IVideoAdaptionResultOperator::class) {
                subclass(
                    MultiContainerThresholdResultOperator::class,
                    MultiContainerThresholdResultOperator.serializer()
                )
            }
        }
    }

    inline fun <reified T: IAdaptionData> encodeToJsonElement(value: T): JsonElement? = try {
        checkClazz<T>()
        localJson.encodeToJsonElement(value)
    } catch (t: Throwable) {
        t.logThrowable()
        null
    }

    inline fun <reified T: IAdaptionData> encodeToString(data: T) = try {
        checkClazz<T>()
        localJson.encodeToString(data)
    } catch (t: Throwable) {
        t.logThrowable()
        ""
    }

    inline fun <reified T: IAdaptionData> decodeFromString(string: String): T? = try {
        checkClazz<T>()
        localJson.decodeFromString<T>(string)
    } catch (t: Throwable) {
        t.logThrowable()
        null
    }

    inline fun <reified T: IAdaptionData> checkClazz() {
//        if (T::class !in testClazzList.keys)  {
//            IllegalArgumentException(T::class.toString()).logThrowable()
//        }
    }

    fun Throwable.logThrowable() {
        if (isTest) throw this
        println(this.toString())
    }
}