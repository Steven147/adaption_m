package com.ss.android.ugc.aweme.videoadaption.adaptionmanager

import com.ss.android.ugc.aweme.adaptionmonitor.AbstractAdaptionManager
import com.ss.android.ugc.aweme.adaptionmonitor.AdaptionNodeType
import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionContext
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.AdaptionPaddingValues
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionResult
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.paramsoperator.VideoProgressAdaptionParams

/**
 * Created by linshaoqin on 17/4/24
 * @author linshaoqin@bytedance.com
 */
class VideoProgressAdaptionManager(
    override val name: String,
    override val context: IAdaptionContext,
    private val minWidth: Int? = null,
    private val maxWidth: Int? = null,
    private val minHeight: Int? = null,
    private val maxHeight: Int? = null,
    private val minX: Float? = null,
    private val maxX: Float? = null,
    private val minY: Float? = null,
    private val maxY: Float? = null,
) : AbstractAdaptionManager<VideoProgressAdaptionParams, VideoAdaptionResult>(), IProgressManger {
    override var cachedProgress: Float? = null
    override val nodeType: AdaptionNodeType = AdaptionNodeType.MANAGER

    override fun doAdaption(params: VideoProgressAdaptionParams) {
        result = getResultWithProgress(
            params.initResult,
            params.finalResult,
            cachedProgress,
        )
        result.noticeListeners()
    }

    //
    private fun getResultWithProgress(
        initResult: VideoAdaptionResult,
        finalResult: VideoAdaptionResult?,
        progress: Float?,
    ): VideoAdaptionResult {
        finalResult ?: return initResult
        progress ?: return initResult

        val newWidth = if (minWidth != null && maxWidth != null) {
            if (minWidth > maxWidth) return initResult
            ((finalResult.width - initResult.width).coerceIn(minWidth, maxWidth) * progress + initResult.width).toInt()
        } else initResult.width

        val newHeight = if (minHeight != null && maxHeight != null) {
            if (minHeight > maxHeight) return initResult
            ((finalResult.height - initResult.height).coerceIn(
                minHeight,
                maxHeight,
            ) * progress + initResult.height).toInt()
        } else initResult.height

        val initTransX = initResult.translateX
        val finalTransX = finalResult.translateX
        val newTranslateX = if (minX != null && maxX != null && initTransX != null && finalTransX != null) {
            if (minX > maxX) return initResult
            ((finalTransX - initTransX).coerceIn(minX, maxX) * progress + initTransX)
        } else initResult.translateX

        val initTransY = initResult.translateY
        val finalTransY = finalResult.translateY
        val newTranslateY = if (minY != null && maxY != null && initTransY != null && finalTransY != null) {
            if (minY > maxY) return initResult
            ((finalTransY - initTransY).coerceIn(minY, maxY) * progress + initTransY)
        } else initResult.translateY

        return VideoAdaptionResult(
            width = newWidth,
            height = newHeight,
            translateX = newTranslateX,
            translateY = newTranslateY,
        )
    }
}

//
class EdgeProgressAdaptionManager(
    override val name: String,
    private val finalResult: AdaptionPaddingValues,
    override val context: IAdaptionContext,
): AbstractAdaptionManager<AdaptionPaddingValues, AdaptionPaddingValues>(), IProgressManger {
    override var cachedProgress: Float? = null
    override val nodeType: AdaptionNodeType = AdaptionNodeType.MANAGER

    override fun doAdaption(params: AdaptionPaddingValues) {
        val progress = cachedProgress ?: 0f
        result = AdaptionPaddingValues(
            top = (finalResult.top - params.top).times(progress).toInt() + params.top,
            bottom = (finalResult.bottom - params.bottom).times(progress).toInt() + params.bottom,
            left = (finalResult.left - params.left).times(progress).toInt() + params.left,
            right = (finalResult.right - params.right).times(progress).toInt()  + params.right,
        )
        result.noticeListeners()
    }
}

interface IProgressManger {
    var cachedProgress: Float?
}