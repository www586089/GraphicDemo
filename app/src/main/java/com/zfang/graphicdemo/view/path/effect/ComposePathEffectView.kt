package com.zfang.graphicdemo.view.path.effect

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.zfang.graphicdemo.common.px2Dp

/**
 * ComposePathEffect(PathEffect outerpe, PathEffect innerpe)
 * 先将路径变成innerpe的效果，再去复合outerpe的路径效果即：outerpe(innerpe(path))
 */
class ComposePathEffectView(context: Context?, attrs: AttributeSet?) : BasePathView(context, attrs) {
    //动态改变改值会产生动画效果
    var mPhase = 0f
    var circlePath: Path = Path()
    var pathDashPathEffect: PathDashPathEffect? = null
    init {
        circlePath.addCircle(0f, 0f, 2.px2Dp(context!!).toFloat(), Path.Direction.CCW)
        pathDashPathEffect = PathDashPathEffect(circlePath, 6.px2Dp(context).toFloat(), mPhase, PathDashPathEffect.Style.TRANSLATE)
    }

    override fun getPathEffect(): PathEffect? {
//        mPhase++
//        invalidate()
        var cornerPathEffect = CornerPathEffect(strokeWidth.toFloat())
        var dashPathEffect = DashPathEffect(floatArrayOf(30f, 5f), 0f)
        var discretePathEffect = DiscretePathEffect(1f, 5f)

        return ComposePathEffect(dashPathEffect, cornerPathEffect)
    }
}