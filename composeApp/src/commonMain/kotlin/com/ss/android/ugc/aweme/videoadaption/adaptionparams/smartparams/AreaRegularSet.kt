package com.ss.android.ugc.aweme.videoadaption.adaptionparams.smartparams

/**
 * Created by linshaoqin on 21/3/24
 * @author linshaoqin@bytedance.com
 */
abstract class AreaRegularSet<T : AreaRegularRect> constructor(
    val areaRects: List<T>,
    val tag: String,
) {
    // check AreaRegularSet is valid
//    fun isValid(): Boolean {
//        if (areaRects.isEmpty()) return false
//        for (rect in areaRects) {
//            if (!rect.isValid()) return false
//        }
//        return true
//    }
}