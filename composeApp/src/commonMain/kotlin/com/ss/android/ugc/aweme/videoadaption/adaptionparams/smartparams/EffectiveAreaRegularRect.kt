package com.ss.android.ugc.aweme.videoadaption.adaptionparams.smartparams

/**
 * Created by linshaoqin on 2024/3/21
 * @author linshaoqin@bytedance.com
 */
// rect class of video sticker area, can scale and translate
class EffectiveAreaRegularRect(
    val relativeRect: RelativeRect,
    tag: String,
    private val regular: AdaptionRegularResultOperator,
) : AreaRegularRect(tag) {
    override fun toString(): String {
        return "tag:${tag}, rect:${relativeRect}, "
    }

    override fun getRectRegularLeft() = regular.getRectLeft(relativeRect.xMin)
    override fun getRectRegularRight() = regular.getRectRight(relativeRect.xMax)
    override fun getRectRegularTop() = regular.getRectTop(relativeRect.yMin)
    override fun getRectRegularBottom() = regular.getRectBottom(relativeRect.yMax)

    // calculate scale of width by given ObstructedAreaRegularRect
    // only min scale accept by all the target, can forceFit in occlusion rect.
    //
    // target range in [0, 1],
    // trans range in R, nearly all of them equal 0.
    // scale range in [0, +inf]
    fun getRectWidthScaleByTargetRect(
        obstructedAreaRect: ObstructedAreaRegularRect,
    ): List<Double> {
        val rectTopToObstructedBottomScale = toWidthScale(
            getScaleByTarget(
                relativeRect.yMin,
                obstructedAreaRect.getRectRegularBottom(),
                regular.getTranslateYRegular(),
            ),
        )
        val rectBottomToObstructedTopScale = toWidthScale(
            getScaleByTarget(
                relativeRect.yMax,
                obstructedAreaRect.getRectRegularTop(),
                regular.getTranslateYRegular(),
            ),
        )
        val rectLeftToObstructedRightScale = getScaleByTarget(
            relativeRect.xMin,
            obstructedAreaRect.getRectRegularRight(),
            regular.getTranslateXRegular(),
        )
        val rectRightToObstructedLeftScale = getScaleByTarget(
            relativeRect.xMax,
            obstructedAreaRect.getRectRegularLeft(),
            regular.getTranslateXRegular(),
        )
        return listOf(
            rectLeftToObstructedRightScale,
            rectTopToObstructedBottomScale,
            rectRightToObstructedLeftScale,
            rectBottomToObstructedTopScale,
        )
//        return minOf(
//            // get width scale meet target
//            toWidthScale(
//                getScaleByTarget(
//                    relativeRect.yMin,
//                    obstructedAreaRect.getRectRegularBottom(),
//                    regular.getTranslateYRegular(),
//                ),
//            ),
//            toWidthScale(
//                getScaleByTarget(
//                    relativeRect.yMax,
//                    obstructedAreaRect.getRectRegularBottom(),
//                    regular.getTranslateYRegular(),
//                ),
//            ),
//            getScaleByTarget(
//                relativeRect.xMin,
//                obstructedAreaRect.getRectRegularLeft(),
//                regular.getTranslateXRegular(),
//            ),
//            getScaleByTarget(
//                relativeRect.xMax,
//                obstructedAreaRect.getRectRegularRight(),
//                regular.getTranslateXRegular(),
//            ),
//        ) // only min scale accept by all the target
//        if (regularParams.isShrink(result)) {
//            onInfo("[getRectWidthScaleByTargetRect] isShrink to avoid area:${obstructedAreaRect.tag}", params)
////            saveParamsToAweme(params.specialParams?.aweme, obstructedAreaRect.tag)
//        }
//        return result
    }

    // calculate transY
    fun getRectTransYByTargetRect(
        obstructedAreaRect: ObstructedAreaRegularRect,
    ): List<Double> {
        return listOf(
            this.getRectRegularTop() - obstructedAreaRect.getRectRegularBottom(),
            this.getRectRegularBottom() - obstructedAreaRect.getRectRegularTop(),
        )
    }

    // change height scale to width scale
    private fun toWidthScale(heightScaleRegular: Double) = heightScaleRegular / regular.videoScaleRegular


    //
    private fun getScaleByTarget(loc: Double, target: Double, trans: Double): Double {
        // x * ws + xb + xt = target
        // xb = (1 - ws) / 2
        // --> get ws

        // y * hs + yb + yt = target
        // yb = (1 - hs) / 2
        // --> get hs
        return (2 * target - 2 * trans - 1) / (2 * loc - 1)
    }

}