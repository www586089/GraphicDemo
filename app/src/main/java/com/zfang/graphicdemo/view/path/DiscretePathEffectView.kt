package com.zfang.graphicdemo.view.path

import android.content.Context
import android.graphics.CornerPathEffect
import android.graphics.DiscretePathEffect
import android.graphics.PathEffect
import android.util.AttributeSet

/**
 * DiscretePathEffect会在路径上绘制一些突出的“杂点”来模拟一种类似铁生锈的效果
 * 参数1表示“杂点”的密集程度，值越小就越密集。
 * 参数2表示“杂点”的大小，值越大杂点越突出，反之则越不突出。
 */
class DiscretePathEffectView(context: Context?, attrs: AttributeSet?) : BasePathView(context, attrs) {

    override fun getPathEffect(): PathEffect? {
        return DiscretePathEffect(1.0f, 5.0f)
    }
}