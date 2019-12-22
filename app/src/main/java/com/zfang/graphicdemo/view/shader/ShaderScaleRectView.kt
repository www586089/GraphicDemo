package com.zfang.graphicdemo.view.shader

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.graphics.transform
import com.zfang.graphicdemo.common.px2Dp

/**
 * 颜色渐变----放大
 */
class ShaderScaleRectView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val TAG = "ShaderScaleRectView"
    private var canvasHeight = 0f
    private var canvasWidth = 0f
    private var rectPaint = Paint()
    private var dashPaint = Paint()
    private var rectF = RectF()
    private var shader: Shader? = null
    private var cMatrix = Matrix()
    private var animatedRectF = RectF()
    private var originRect = RectF()


    val paddingH = 12.px2Dp(context!!).toFloat()
    val paddingV = 6.px2Dp(context!!).toFloat()
    var rectWidth = 0f
    var rectHeight = 0f
    val rLeft = paddingH
    val rTop = paddingV

    init {
        rectPaint.style = Paint.Style.FILL
        rectPaint.color = Color.RED
        setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                startAnimation()
            }
        })

        dashPaint.pathEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        dashPaint.strokeWidth = 1.px2Dp(context!!).toFloat()
        dashPaint.style = Paint.Style.STROKE
        dashPaint.color = Color.BLACK
    }

    private fun startAnimation() {

        val radius = if (rectF.width() > rectF.height()) {
            rectF.width()
        } else {
            rectF.height()
        }
        val animate = ValueAnimator.ofFloat(0f, radius)
        animate.setDuration(3_000)
        animate.addUpdateListener {
            val value = it.animatedValue as Float
            val scale = value / radius
            Log.e("zfang", "value = $value, scale = $scale")
            cMatrix.setScale(scale, scale ,paddingH, rectHeight / 2)

            animatedRectF.set(originRect)
            animatedRectF.transform(cMatrix)
            Log.e(TAG, "animatedRectF.width = ${animatedRectF.width()}")
            invalidate()
        }
        animate.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        rectF.set(rLeft, rTop, rLeft + rectWidth, rTop + rectHeight)
        if (null == shader) {
            shader = LinearGradient(paddingH, 0f, paddingH + rectWidth, 0f, Color.RED, Color.GREEN, Shader.TileMode.CLAMP)
            animatedRectF.set(paddingH, rTop, paddingH + rectWidth, rTop + rectHeight)
            originRect.set(animatedRectF)
            rectPaint.shader = shader
        }
        canvas!!.drawRect(animatedRectF, rectPaint)
        canvas.drawRect(rectF, dashPaint)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        canvasWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        canvasHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()

        rectWidth = canvasWidth / 2
        rectHeight = rectWidth / 2
    }
}