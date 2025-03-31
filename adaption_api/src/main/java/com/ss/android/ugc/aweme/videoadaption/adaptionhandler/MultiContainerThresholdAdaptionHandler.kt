package com.ss.android.ugc.aweme.videoadaption.adaptionhandler

import com.ss.android.ugc.aweme.adaptionmonitor.IAdaptionContext
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.IAdaptionHandlerContext
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.IAdaptionScreenContext
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.IAdaptionStrategyContext
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.IThresholdListContext
import com.ss.android.ugc.aweme.videoadaption.adaptioncontext.MultiContainerThresholdHandlerContext
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.ScreenArea.Companion.bottomAreaList
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.ScreenArea.Companion.sumHeight
import com.ss.android.ugc.aweme.videoadaption.adaptionhandler.ScreenArea.Companion.topAreaList
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionParams
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.VideoAdaptionResult
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.isDimensionValid
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.paramsoperator.IContainerThresholdParamsOperator
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.paramsoperator.IScaleTypeParamOperator
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.resultoperator.IMultiContainerThresholdResultOperator
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.resultoperator.IThresholdAdaptionResultOperator
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.resultoperator.MultiContainerThresholdResultInnerOperator
import com.ss.android.ugc.aweme.videoadaption.adaptionparams.resultoperator.MultiContainerThresholdResultOperator
import kotlinx.serialization.Serializable

/**
 * @author linshaoqin
 *
 * video adaption with nearest container, then do threshold adaption
 */
//
class MultiContainerThresholdAdaptionHandler(
    override val context: IAdaptionHandlerContext
) : AbstractAdaptionHandler() {
    override val name: String = "multi_container_handler"

    companion object {
        private var instance: MultiContainerThresholdAdaptionHandler? = null

        @JvmStatic
        fun getOrCreate(context: IAdaptionHandlerContext) =
            instance ?: MultiContainerThresholdAdaptionHandler(context).apply { instance = this@apply }
    }

    override fun handleAdaption(
        oldResult: VideoAdaptionResult?,
        params: VideoAdaptionParams
    ): VideoAdaptionResult? {
        if (!params.isDimensionValid()) {
            fail(params=params) { "!params.isDimensionValid()" }
            return null
        }

        val feedScreenContext = (context.strategyContext as? IAdaptionScreenContext)?.feedScreenContext
        if (feedScreenContext == null) { // check in strategy, never reach
            fail(context=context) { "screenContext == null" }
            return null
        }
        // threshold override logic
//        val widthThreshold: Float
//        val heightThreshold: Float
        val paramsOperator = params.paramsOperator
        val forceThreshold = (paramsOperator as? IScaleTypeParamOperator)?.forceScaleType?.threshold
//        if (forceThreshold != null) {
//            widthThreshold = forceThreshold
//            heightThreshold = forceThreshold
//        } else {
//            widthThreshold = context.widthThreshold
//            heightThreshold = context.heightThreshold
//        }

        // container override logic
        val videoWidth = params.videoWidth
        val videoHeight = params.videoHeight

        // collect all possible container result
        val topTypeList = feedScreenContext.topTypeList
        val bottomTypeList = feedScreenContext.bottomTypeList
        val screenWidth = feedScreenContext.screenWidth
        val screenHeight = feedScreenContext.screenHeight
        val resultList = mutableListOf<IMultiContainerThresholdResultOperator>()
        val topTypeCnt = topTypeList.size
        val bottomTypeCnt = bottomTypeList.size
        if (topTypeCnt != bottomTypeCnt) {
            fail(context=context) { "topTypeCnt != bottomTypeCnt" }
            return null
        }
        for (index in 0 until topTypeCnt) {
            val topType = topTypeList[index] // never fail
            val bottomType = bottomTypeList[index] // never fail
            val topHeight = topAreaList.sumHeight(topType, feedScreenContext)
            val bottomHeight = bottomAreaList.sumHeight(bottomType, feedScreenContext)
            val adjustContainerHeight = screenHeight - topHeight - bottomHeight
            resultList.add(
                MultiContainerThresholdResultInnerOperator(
                    topType = topType,
                    bottomType = bottomType,
                    topHeight = topHeight,
                    bottomHeight = bottomHeight,
                    adjustContainerHeight = adjustContainerHeight,
                    adjustContainerWidth = screenWidth
                )
            )
        }
        if (resultList.isEmpty()) {
            fail { "resultList.isEmpty" }
            return null  // todo check
        }
        val thresholdListContext = context as? IThresholdListContext
        if (thresholdListContext == null) {
            fail(context=context) { "thresholdListContext == null" }
            return null
        }
        val widthThresholds = thresholdListContext.widthThresholds.map { forceThreshold ?: it }
        val heightThresholds = thresholdListContext.heightThresholds.map { forceThreshold ?: it }
        if (widthThresholds.size != topTypeCnt || heightThresholds.size != topTypeCnt) {
            fail(context=context) { "thresholds.size != bottomTypeCnt" }
            return null
        }
        val containerThresholds = (paramsOperator as? IContainerThresholdParamsOperator)?.containerThresholds
        if (containerThresholds == null || containerThresholds.size != (topTypeCnt - 1)) {
            fail(params=params) { "containerThreshold == null || containerThresholds.size != (topTypeCnt -1)" }
            return null
        }
        val increaseResultList = resultList.toMutableList().apply { sortBy { it.adjustContainerRatio } }
        val decreaseResultList = increaseResultList.reversed() // return a copy
        val maxContainerRatioResult = increaseResultList.last() // never null, not empty
        val minContainerRatioResult = increaseResultList.first()

        // choose target result
        val thresholdOperator: IThresholdAdaptionResultOperator
        val containerResultOperator: IMultiContainerThresholdResultOperator
        if (params.videoRatio >= maxContainerRatioResult.adjustContainerRatio) {
            containerResultOperator = maxContainerRatioResult
            thresholdOperator = ThresholdAdaptionHandler.getThresholdAdaptionResultOperator(
                containerWidth = screenWidth.toInt(),
                containerHeight = containerResultOperator.adjustContainerHeight.toInt(),
                videoWidth = videoWidth,
                videoHeight = videoHeight,
                widthThreshold = widthThresholds.last(),
                heightThreshold = heightThresholds.last()
            )
        } else if (params.videoRatio < minContainerRatioResult.adjustContainerRatio) {
            containerResultOperator = minContainerRatioResult
            thresholdOperator = ThresholdAdaptionHandler.getThresholdAdaptionResultOperator(
                containerWidth = screenWidth.toInt(),
                containerHeight = containerResultOperator.adjustContainerHeight.toInt(),
                videoWidth = videoWidth,
                videoHeight = videoHeight,
                widthThreshold = widthThresholds.first(),
                heightThreshold = heightThresholds.first()
            )
        } else {
            // min <= videoRatio < max
            // todo sort better method
            val upperResult = increaseResultList.first { it.adjustContainerRatio > params.videoRatio }
            val lowerResult = decreaseResultList.first { it.adjustContainerRatio <= params.videoRatio }
            val lowerIndex = increaseResultList.indexOfFirst { it.adjustContainerRatio <= params.videoRatio }
            val curContainerThreshold = containerThresholds[lowerIndex] // never fail
            // lower <= videoRatio < upper
            val threshold = lowerResult.adjustContainerRatio +
                (upperResult.adjustContainerRatio - lowerResult.adjustContainerRatio) * curContainerThreshold
            containerResultOperator = if (params.videoRatio < threshold) lowerResult else upperResult
            thresholdOperator = ThresholdAdaptionHandler.getThresholdAdaptionResultOperator(
                containerWidth = screenWidth.toInt(),
                containerHeight = containerResultOperator.adjustContainerHeight.toInt(),
                videoWidth = videoWidth,
                videoHeight = videoHeight,
                widthThreshold = AdaptionScaleType.CROP.threshold,
                heightThreshold = AdaptionScaleType.CROP.threshold
            )
        }

        // get threshold result
        val result = ThresholdAdaptionHandler.getThresholdVideoAdaptionResult(
            containerWidth = containerResultOperator.adjustContainerWidth.toInt(),
            containerHeight = containerResultOperator.adjustContainerHeight.toInt(),
            videoWidth = videoWidth,
            videoHeight = videoHeight,
            resultOperator = MultiContainerThresholdResultOperator(
                topType = containerResultOperator.topType,
                bottomType = containerResultOperator.bottomType,
                topHeight = containerResultOperator.topHeight,
                bottomHeight = containerResultOperator.bottomHeight,
                adjustContainerHeight = containerResultOperator.adjustContainerHeight,
                adjustContainerWidth = containerResultOperator.adjustContainerWidth,
                adaptionScaleType = thresholdOperator.adaptionScaleType,
                areaDiff = thresholdOperator.areaDiff,
                alignType = thresholdOperator.alignType,
            ),
        )

        return oldResult?.copy(
            width = result.width,
            height = result.height,
            resultOperator = result.resultOperator
        ) ?: result
    }

}


// screen params in all scene
interface IScreenParams {
    // get status bar height
    val statusBarHeight: Float
    // get screen width
    val screenWidth: Float
    // get screen height
    val screenHeight: Float
}

//
interface IScreenParamsTypeConfig {
    // [000, 001, 011].., indicate top area type
    val topTypeList: List<Int>
    // [000, 001, 011]..., indicate bottom area type
    val bottomTypeList: List<Int>
}

// screen params in feed scene,
interface IFeedScreenContext: IScreenParams, IScreenParamsTypeConfig, IAdaptionContext {
    //
    val topTabHeight: Float
    //
    val bottomBannerHeight: Float
    //
    val bottomTabHeight: Float
}

/**
 * FeedScreenContext
 *
 * use for screen params and edge params
 */
@Serializable
data class FeedScreenContext(
    override val statusBarHeight: Float,
    override val screenWidth: Float,
    override val screenHeight: Float,
    override val topTabHeight: Float,
    override val bottomBannerHeight: Float,
    override val bottomTabHeight: Float,
    override val topTypeList: List<Int>,
    override val bottomTypeList: List<Int>,
) : IFeedScreenContext

//
enum class ScreenAreaDirection {
    BOTTOM, TOP
}

// todo: solve ScreenArea and FeedScreenContext relation
enum class ScreenArea(val typeName: String, private val maskValue: Int, private val direction: ScreenAreaDirection) {
    STATUS_BAR("status bar", 0, ScreenAreaDirection.TOP), // 01
    TOP_TAB("top tab", 1, ScreenAreaDirection.TOP), // 11

    BOTTOM_TAB("bottom tab", 0, ScreenAreaDirection.BOTTOM), // 01
    BANNER("banner",  1, ScreenAreaDirection.BOTTOM); // 11


//    private fun getHeight(adaptionType: Int, height: Int): Int =
//        if (getVisibilityByType(adaptionType) != 0) height else 0

    //
    fun getHeight(params: IFeedScreenContext): Float {
        return when (this) {
            STATUS_BAR -> params.statusBarHeight
            TOP_TAB -> params.topTabHeight
            BANNER -> params.bottomBannerHeight
            BOTTOM_TAB -> params.bottomTabHeight
        }
    }

    //
    fun getVisibility(adaptionType: Int) = 1 and (adaptionType shr maskValue)

    companion object {

        fun topTypeList() = getSingleTypeList(topAreaList.size)

        fun bottomTypeList() = getSingleTypeList(bottomAreaList.size)

        private fun getSingleTypeList(size: Int): List<Int> {
            val list = mutableListOf<Int>()
            for (index in 0 until size + 1) {
                list.add((1 shl index) - 1)
            }
            return list
        }

        private val areaList = listOf(STATUS_BAR, TOP_TAB, BANNER, BOTTOM_TAB)
        val topAreaList = areaList.filter {
            it.direction == ScreenAreaDirection.TOP
        }.sortedBy { it.maskValue }

        val bottomAreaList = areaList.filter {
            it.direction == ScreenAreaDirection.BOTTOM
        }.sortedBy { it.maskValue }


        fun List<ScreenArea>.sumHeight(adaptionType: Int, params: IFeedScreenContext) =
            this.map { it.getVisibility(adaptionType) * it.getHeight(params) }.sum()


        fun Int.toCount() = (this + 1).div(2)

        // value means 1's count
        // count from low to high
        // cnt / value
        // 0 - 00 - 0
        // 1 - 01 - 1
        // 2 - 11 - 3
        fun Int.toType() = (1 shl this) - 1
//
//
//        fun getTopHeight(topType: Int, params: IFeedScreenContext) = topAreaList.sumHeight(topType, params)
//        fun getBottomHeight(bottomType: Int, params: IFeedScreenContext) = bottomAreaList.sumHeight(bottomType, params)
//
        fun getTopLineName(type: Int) = topAreaList.getLineName(type)
        fun getBottomLineName(type: Int) = bottomAreaList.getLineName(type)

        private fun List<ScreenArea>.getLineName(adaptionType: Int): String {
            var name = "none"
            for (item in this) { // todo remove circulation, use bits calculation
                if (item.getVisibility(adaptionType) == 0) {
                    break
                } else {
                    name = item.typeName
                }
            }
            return name
        }
    }
}