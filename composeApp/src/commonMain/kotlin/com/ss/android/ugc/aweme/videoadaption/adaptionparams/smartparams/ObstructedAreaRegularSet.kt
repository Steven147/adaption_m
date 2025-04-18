package com.ss.android.ugc.aweme.videoadaption.adaptionparams.smartparams

/**
 * Created by linshaoqin on 2024/3/21
 * @author linshaoqin@bytedance.com
 */
// if [EffectiveAreaRegularRect] can satisfy one white rect in [ObstructedAreaRegularSet], means satisfy this set (like desc occlusion set)
class ObstructedAreaRegularSet constructor(
    areaRect: List<ObstructedAreaRegularRect>, tag: String
) : AreaRegularSet<ObstructedAreaRegularRect>(areaRect, tag) {

    /**
     * init with single black rect / ObstructedAreaRegularRect
     */
// 3. turn blackOcclusionRect into whiteOcclusionRects do 1
//    private constructor(blackObstructedAreaRect: ObstructedAreaRegularRect, tag: String) : super(
//        blackObstructedAreaRect.intoWhiteRects(), tag,
//    )

// 4. turn blackRect into blackOcclusionRect and do 3
//    constructor(blackRect: Rect, containerHeight: Int, containerWidth: Int, tag: String) : this(
//        blackRect.intoAreaRect(containerHeight, containerWidth), tag,
//    )

// input blackRect: List<Rect>

    /**
     * init with multiple white Rects
     */
    // 1. [super] init whiteOcclusionRects

    // 2. turn rects into whiteOcclusionRects and do 1
//    private constructor(rects: List<Rect>, containerHeight: Int, containerWidth: Int, tag: String) : super(
//        rects.intoAreaRects(containerHeight, containerWidth), tag,
//    )


    companion object {

        // change black rect into 4 white rects
//        private fun ObstructedAreaRegularRect.intoWhiteRects() = listOf(
//            ObstructedAreaRegularRect(tag = "${tag}_Left", xMax = this@intoWhiteRects.getRectRegularLeft()), // white rect on left
//            ObstructedAreaRegularRect(tag = "${tag}_Right", xMin = this@intoWhiteRects.getRectRegularRight()), // white rect on right
//            ObstructedAreaRegularRect(tag = "${tag}_Top", yMax = this@intoWhiteRects.getRectRegularTop()), // white rect on top
//            ObstructedAreaRegularRect(tag = "${tag}_Bottom", yMin = this@intoWhiteRects.getRectRegularBottom()), // white rect on bottom
//        ).filter { it.isValid() } // filter valid rect

//        private fun Rect.intoAreaRect(containerHeight: Int, containerWidth: Int) =
//            ObstructedAreaRegularRect(this@intoAreaRect, containerHeight, containerWidth, "black_tag_tmp")
//
//        private fun List<Rect>.intoAreaRects(containerHeight: Int, containerWidth: Int) =
//            this@intoAreaRects.map { it.intoAreaRect(containerHeight, containerWidth) }
    }
}