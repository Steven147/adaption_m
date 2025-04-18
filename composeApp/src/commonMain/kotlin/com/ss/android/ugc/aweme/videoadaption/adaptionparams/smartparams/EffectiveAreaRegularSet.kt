package com.ss.android.ugc.aweme.videoadaption.adaptionparams.smartparams

/**
 * Created by linshaoqin on 21/3/24
 * @author linshaoqin@bytedance.com
 */
class EffectiveAreaRegularSet(
    areaRects: List<EffectiveAreaRegularRect>, tag: String
) : AreaRegularSet<EffectiveAreaRegularRect>(areaRects, tag) {
    // todo simplify to the most simple rect
    fun simplify() = this.apply {

    }
}