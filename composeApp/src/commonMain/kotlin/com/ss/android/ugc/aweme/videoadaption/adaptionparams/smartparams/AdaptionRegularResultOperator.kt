package com.ss.android.ugc.aweme.videoadaption.adaptionparams.smartparams

import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionParams
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionResult
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.resultoperator.IVideoAdaptionResultOperator


/**
 * Created by linshaoqin on 2024/3/22
 * @author linshaoqin@bytedance.com
 */
data class AdaptionRegularResultOperator(
    /**
     * input value
     */
    private var params: VideoAdaptionParams,
    private var oldResult: VideoAdaptionResult,
) : IVideoAdaptionResultOperator {
    // smart crop handler in smart crop scene
    var isIntextOut: Boolean? = null
    var isIntextOccludeTab: Boolean? = null
    var isIntextOccludeInteraction: Boolean? = null
    var isIntextOccludeDesc: Boolean? = null
    var needSave: Boolean = false

    /**
     * output value (result of adaptation)
     */
    fun getWidthScaleRegular() = widthScaleRegular

    // width is shrink
    fun isShrink() = widthScaleRegular < oldWidthScaleRegular

    // get translate center point for get target width
    fun getTranslateYRegular() = transYRegular

    // get translate center point for get target width
    fun getTranslateXRegular() = transXRegular

    // save regular result
    fun updateRegularResult(
        widthScaleRegular: Double? = null,
        transXRegular: Double? = null,
        transYRegular: Double? = null
    ) {
        if (widthScaleRegular != null) {
            this.widthScaleRegular = widthScaleRegular
        }
        if (transXRegular != null) {
            this.transXRegular = transXRegular
        }
        if (transYRegular != null) {
            this.transYRegular = transYRegular
        }
    }

    var height: Int
        get() = (heightScaleRegular * params.containerHeight).toInt()
        private set(value) {}
    var width: Int
        get() = (widthScaleRegular * params.containerWidth).toInt()
        private set(value) {}

    var translateX: Float
        get() = (transXRegular * params.containerWidth).toFloat()
        private set(value) {}

    var translateY: Float
        get() = (transYRegular * params.containerHeight).toFloat()
        private set(value) {}

    /**
     * new value (regular result of adaptation)
     *
     * width fixed:         2 * xBiasRegular + marginStartRegular + widthScaleRegular + marginEndRegular = 1
     * height fixed:        2 * yBiasRegular + marginTopRegular + heightScaleRegular + marginBottomRegular = 1
     * video scale fixed    widthScaleRegular * containerWidth / heightScaleRegular * containerHeight = oldWidth / oldHeight
     *                   => heightScaleRegular / widthScaleRegular = videoScale / containerScale = videoRegularScale
     *
     * core 3 value, can calculate all the others
     */
    private var widthScaleRegular: Double = oldWidthScaleRegular
    private var transXRegular: Double = 0.0 // default in center
    private var transYRegular: Double = 0.0 // default in center

    private var heightScaleRegular: Double
        get() = widthScaleRegular * videoScaleRegular
        private set(value) {}
    private var xBiasRegular
        get() = (1 - widthScaleRegular) / 2
        private set(value) {}
    private var yBiasRegular
        get() = (1 - heightScaleRegular) / 2
        private set(value) {}

    // get rect in container
    fun getRectLeft(xMin: Double) = xMin * widthScaleRegular + xBiasRegular + transXRegular

    //
    fun getRectRight(xMax: Double) = xMax * widthScaleRegular + xBiasRegular + transXRegular

    //
    fun getRectTop(yMin: Double) = yMin * heightScaleRegular + yBiasRegular + transYRegular

    //
    fun getRectBottom(yMax: Double) = yMax * heightScaleRegular + yBiasRegular + transYRegular

    //
    fun alignToBottomTransY() = yBiasRegular

//    //
//    fun isScaleValid(scale: Double) = scale > 0.0 && scale < widthScaleRegular

    /**
     * init value (begin of adaption)
     * regular means relative to container
     *      containerScaleRegular = containerHeightRegular = containerWidthRegular = 1.0,
     */
    // video / container params
    private var videoScale: Double
        get() = oldResult.height.toDouble() / oldResult.width
        private set(value) {}
    private var containerScale: Double
        get() = params.containerHeight.toDouble() / params.containerWidth
        private set(value) {}
    var videoScaleRegular: Double // video (height / width) value when container regular to [1 * 1],
        get() = videoScale / containerScale
        private set(value) {}

    // old video adaption regular params
    private var oldWidthScaleRegular: Double //
        get() = oldResult.width.toDouble() / params.containerWidth
        private set(value) {}
//    private var oldTranslateXRegular: Double //
//        get() = (oldResult.translateX ?: 0).toDouble() / params.containerWidth
//        private set(value) {}
//    private var oldTransYRegular: Double //
//        get() = (oldResult.translateY ?: 0).toDouble() / params.containerHeight
//        private set(value) {}
}



