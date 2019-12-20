package com.zfang.graphicdemo.view.path.effect

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.zfang.graphicdemo.common.px2Dp

abstract class BasePathView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var canvasWidth :Int = 0
    private var canvasHeight: Int = 0
    private var paint = Paint()
    private var textPaint = TextPaint()
    private var path: Path = Path()
    private var rect = Rect()
    protected var strokeWidth = 4.px2Dp(getContext())

    init {
        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        paint.isAntiAlias = true
        paint.strokeWidth = strokeWidth.toFloat()

        textPaint.color = Color.DKGRAY
        textPaint.textSize = 16.px2Dp(context!!).toFloat()

    }

    private fun initPath() {
        path.moveTo(0f, 0f)
        for (index in 1..30) {
            var sign = Math.random()
            if (sign < 0.5f) {
                sign = -sign
            }
            var y =
            path.lineTo((index * 35).toFloat(), (30.px2Dp(context) * sign).toFloat())
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

//        Log.e("zfang", "width = $canvasWidth, height = $canvasHeight")
        if (path.isEmpty) {
            return
        }

        canvas!!.save()
        canvas.translate(0f, (canvasHeight / 2).toFloat())
        canvas.drawColor(Color.parseColor("#1167c8ff"))
        paint.pathEffect = getPathEffect()
        canvas.drawPath(path, paint)
        canvas.drawCircle(0f, 0f, 40.px2Dp(context).toFloat(), paint)
        canvas.restore()

        canvas.save()
        val effectName = getEffectName()
        textPaint.getTextBounds(effectName, 0, effectName.length, rect)
        canvas.translate(((canvasWidth - rect.width()) / 2).toFloat(), rect.height().toFloat())
        canvas.drawText(effectName, 0f, 0f, textPaint)
        canvas.restore()
    }

    fun getEffectName(): String {
        var effectName = "No PathEffect"
        val pathEffect = paint.pathEffect
        when (pathEffect) {
            is DashPathEffect -> effectName = "DashPathEffect"
            is CornerPathEffect -> effectName = "CornerPathEffect"
            is DiscretePathEffect -> effectName = "DiscretePathEffect"
            is PathDashPathEffect -> effectName = "PathDashPathEffect"
            is ComposePathEffect -> effectName = "ComposePathEffect"
            is SumPathEffect -> effectName = "SumPathEffect"
        }

        return effectName
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightMeasureSpecY = MeasureSpec.makeMeasureSpec(100.px2Dp(context), MeasureSpec.EXACTLY)
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpecY)
        if (measuredWidth > 0 && path.isEmpty) {
            canvasWidth = measuredWidth
            canvasHeight = 100.px2Dp(context)
            initPath()
            paint.pathEffect = getPathEffect()
        }
    }

    abstract fun getPathEffect(): PathEffect?
}