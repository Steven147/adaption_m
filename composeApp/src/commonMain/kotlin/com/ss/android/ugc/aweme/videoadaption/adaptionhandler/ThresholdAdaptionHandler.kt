package com.ss.android.ugc.aweme.videoadaption.adaptionhandler

import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.IAdaptionHandlerContext
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.IThresholdContext
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.ThresholdAdaptionHandlerContext
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionParams
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionResult
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.isDimensionValid
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.paramsoperator.IScaleTypeParamOperator
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.resultoperator.IThresholdAdaptionResultOperator
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.resultoperator.ThresholdAdaptionResultOperator

/**
 * Created by linshaoqin on 8/12/23
 * @author linshaoqin@bytedance.com
 *
 * threshold adaption strategy
 */
class ThresholdAdaptionHandler(
    override val context: IAdaptionHandlerContext
) : AbstractAdaptionHandler() {
    override val name = "threshold_handler"

    override fun handleAdaption(
        oldResult: VideoAdaptionResult?,
        params: VideoAdaptionParams
    ): VideoAdaptionResult? {
        // init params / old result
        if (!params.isDimensionValid()) {
            fail { "!params.isDimensionValid()" }
            return null
        }

        // threshold override logic
        val widthThreshold: Float
        val heightThreshold: Float
        val forceThreshold = (params.paramsOperator as? IScaleTypeParamOperator)?.forceScaleType?.threshold
        if (context !is IThresholdContext) {
            fail { "context is not IThresholdContext" }
            return null
        }
        if (forceThreshold != null) {
            widthThreshold = forceThreshold
            heightThreshold = forceThreshold
        } else {
            widthThreshold = context.widthThreshold
            heightThreshold = context.heightThreshold
        }

        // container override logic
        val containerWidth = params.containerWidth
        val containerHeight = params.containerHeight
        val videoWidth = params.videoWidth
        val videoHeight = params.videoHeight

        // choose result by threshold
        val resultOperator = getThresholdAdaptionResultOperator(
            containerWidth=containerWidth,
            containerHeight=containerHeight,
            videoWidth=videoWidth,
            videoHeight=videoHeight,
            widthThreshold=widthThreshold,
            heightThreshold=heightThreshold
        )

        // get result
        val result = getThresholdVideoAdaptionResult(
            containerWidth=containerWidth,
            containerHeight=containerHeight,
            videoWidth=videoWidth,
            videoHeight=videoHeight,
            resultOperator=resultOperator,
        )

//        Log.d(tag, "config: $config, specialConfig:$specialConfig, result:$result")
        return oldResult?.copy(
            width = result.width,
            height = result.height,
            resultOperator = result.resultOperator
        ) ?: result
    }

    companion object {
        private var instance: ThresholdAdaptionHandler? = null
        fun getOrCreate(context: ThresholdAdaptionHandlerContext): ThresholdAdaptionHandler {
            return instance ?: ThresholdAdaptionHandler(context).apply { instance = this }
        }
        /**
         * common util
         *
         * todo add check isDimensionValid()
         */
        fun getThresholdAdaptionResultOperator(
            containerWidth: Int,
            containerHeight: Int,
            videoWidth: Int,
            videoHeight: Int,
            widthThreshold: Float,
            heightThreshold: Float
        ): IThresholdAdaptionResultOperator {
            val scaleTypeResult: AdaptionScaleType
            val alignTypeResult: AlignType
            val areaDiff: Float
            if (isContainerScaleHigher(containerWidth, containerHeight, videoWidth, videoHeight)) {
                // fat video, forceFit height will lose width
                areaDiff = calWidthAreaLoss(containerWidth, containerHeight, videoWidth, videoHeight).toFloat()
                if (areaDiff > widthThreshold) { // should avoid width loss
                    //                Log.d(tag, "[doAdaptionStrategy] [default adaption] forceFit width, avoid width loss $widthLoss")
                    scaleTypeResult = AdaptionScaleType.FIT
                    alignTypeResult = AlignType.WIDTH
                } else {
                    //                Log.d(tag, "[doAdaptionStrategy] [default adaption] forceFit height, accept width loss $widthLoss")
                    scaleTypeResult = AdaptionScaleType.CROP
                    alignTypeResult = AlignType.HEIGHT
                }
            } else { // thin video, forceFit width will lose height
                areaDiff = calHeightAreaLoss(containerWidth, containerHeight, videoWidth, videoHeight, ).toFloat()
                if (areaDiff > heightThreshold) { // should avoid height loss
                    //                Log.d(tag, "[doAdaptionStrategy] [default adaption] forceFit height, avoid height loss $heightLoss")
                    scaleTypeResult = AdaptionScaleType.FIT
                    alignTypeResult = AlignType.HEIGHT
                } else {
                    //                Log.d(tag, "[doAdaptionStrategy] [default adaption] forceFit width, accept height loss $heightLoss")
                    scaleTypeResult = AdaptionScaleType.CROP
                    alignTypeResult = AlignType.WIDTH
                }
            }
            return ThresholdAdaptionResultOperator(
                adaptionScaleType = scaleTypeResult,
                areaDiff = areaDiff,
                alignType = alignTypeResult,
            )
        }

        fun isContainerScaleHigher(
            containerWidth: Int,
            containerHeight: Int,
            videoWidth: Int,
            videoHeight: Int,
        ) = containerHeight * videoWidth > videoHeight * containerWidth

        fun calWidthAreaLoss(
            containerWidth: Int,
            containerHeight: Int,
            videoWidth: Int,
            videoHeight: Int,
        ): Double {
//        fat video, forceFit height loss width ( videoScale=1.7, containerScale=2.0, loss=0.15)
//        val regularVideoHeight = 1.0                          C
//        val regularVideoWidth = 1.0 / videoScale              D
//        val regularContainerHeight = 1.0                      C
//        val regularContainerWidth = 1.0 / containerScale      B
//        val loss
//        = (C * D - C * B) / C * D
//        = (D - B) / D
//        = (1 - videoScale / containerScale) = 0.15
        return 1.0 - (videoHeight * containerWidth).toDouble() / (containerHeight * videoWidth)
    }

        fun calHeightAreaLoss(
            containerWidth: Int,
            containerHeight: Int,
            videoWidth: Int,
            videoHeight: Int,
        ): Double {
//        fat container, forceFit height loss width ( videoScale=2.0, containerScale=1.7, loss=0.15)
//        val regularVideoHeight = 1.0 * videoScale             D
//        val regularVideoWidth = 1.0                           C
//        val regularContainerHeight = 1.0 * containerScale     B
//        val regularContainerWidth = 1.0                       C
//        val loss
//        = (C * D - C * B) / C * D
//        = (D - B) / D
//        = (1 - containerScale / videoScale) = 0.15
        return 1.0 - (containerHeight * videoWidth).toDouble() / (videoHeight * containerWidth)
    }

        // avoid float calculate
        fun getThresholdVideoAdaptionResult(
            containerWidth: Int,
            containerHeight: Int,
            videoWidth: Int,
            videoHeight: Int,
            resultOperator: IThresholdAdaptionResultOperator,
        ): VideoAdaptionResult {
            val newHeight: Int
            val newWidth: Int
            when (resultOperator.alignType) {
                AlignType.WIDTH -> {
                    newWidth = containerWidth
                    newHeight = containerWidth * videoHeight / videoWidth
                }
                AlignType.HEIGHT -> {
                    newWidth = containerHeight * videoWidth / videoHeight
                    newHeight = containerHeight
                }
            }
            return VideoAdaptionResult(
                width = newWidth, height = newHeight,
                resultOperator = resultOperator,
                translateX = 0f, translateY = 0f,
            )
        }

    }
}

//
enum class AdaptionScaleType {
    FIT,
    CROP;
//    BOTH, // special type
//    FILL_BOUNDS,
//    NONE;

    val threshold: Float
        get() = when (this) {
            CROP -> Float.MAX_VALUE
            FIT -> Float.MIN_VALUE
        }
}

//
enum class AlignType {
    HEIGHT,
    WIDTH
}
