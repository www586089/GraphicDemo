package com.zfang.graphicdemo.view.path

import android.content.Context
import android.graphics.CornerPathEffect
import android.graphics.DashPathEffect
import android.graphics.PathEffect
import android.util.AttributeSet

class DashPathEffectView(context: Context?, attrs: AttributeSet?) : BasePathView(context, attrs) {

    //动态改变改值会产生动画效果
    var mPhase = 0f
    override fun getPathEffect(): PathEffect? {
//        mPhase++
//        invalidate()
        return DashPathEffect(floatArrayOf(10f, 5f), mPhase)
    }
}