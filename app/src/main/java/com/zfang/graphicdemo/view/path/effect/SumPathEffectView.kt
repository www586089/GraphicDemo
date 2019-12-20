package com.zfang.graphicdemo.view.path.effect

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.zfang.graphicdemo.common.px2Dp

/**
 *
 * 两种效果加起来作用于路径
 */
/**
 * SumPathEffect(PathEffect first, PathEffect second)
 * Construct a PathEffect whose effect is to apply two effects, in sequence.
 * (e.g. first(path) + second(path))
 */
class SumPathEffectView(context: Context?, attrs: AttributeSet?) : BasePathView(context, attrs) {
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
        var discretePathEffect = DiscretePathEffect(10f, 1f)

        return SumPathEffect(pathDashPathEffect, dashPathEffect)
    }
}