package com.ss.android.ugc.aweme.videoadaption.adaptionparams.smartparams

/**
 * Created by linshaoqin on 2024/3/21
 * @author linshaoqin@bytedance.com
 */
// rect class of occlusion area
class ObstructedAreaRegularRect constructor(
    private val relativeRect: RelativeRect,
    tag: String
) : AreaRegularRect(
    tag = tag,
) {
    companion object {
        const val LEFT_EDGE_TAG = "left_edge"
        const val RIGHT_EDGE_TAG = "right_edge"
        const val INTERACT_TOP_TAG = "interact_top"
        const val INTERACT_RIGHT_TAG = "interact_right"
        const val INTERACT_LEFT_UP_TAG = "interact_left_up"
        const val INTERACT_LEFT_TAG = "interact_left"
    }

    override fun toString(): String {
        return "tag:${tag}, rect:${relativeRect}, "
    }

//    constructor(
//        xMin: Double = 0.0,
//        xMax: Double = 1.0,
//        yMin: Double = 0.0,
//        yMax: Double = 1.0,
//        tag: String
//    ) : super(
//        RelativeRect(
//            xMin = xMin,
//            xMax = xMax,
//            yMin = yMin,
//            yMax = yMax,
//        ),
//        tag = tag,
//    )

    override fun getRectRegularLeft(): Double = relativeRect.xMin
    override fun getRectRegularRight(): Double = relativeRect.xMax
    override fun getRectRegularTop(): Double = relativeRect.yMin
    override fun getRectRegularBottom(): Double = relativeRect.yMax

}