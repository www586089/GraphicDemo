package com.zfang.graphicdemo.view.shader

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.zfang.graphicdemo.common.px2Dp

/**
 * 定义一个水平渐变的矩形从左到右滑动
 */
class ShaderTranslateView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var canvasHeight = 0f
    private var canvasWidth = 0f
    private var rectPaint = Paint()
    private var dashPaint = Paint()
    private var rectF = RectF()
    private var shader: Shader? = null
    private var cMatrix = Matrix()
    private var animatedRectF = RectF()


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

        val px = -rectF.width() + paddingH
        val animate = ValueAnimator.ofFloat(px, paddingH)
        animate.setDuration(3_000)
        animate.addUpdateListener {
            val value = it.animatedValue as Float
            animatedRectF.set(value, rTop, value + rectWidth, rTop + rectHeight)
            cMatrix.setTranslate(value, 0f)
            shader!!.setLocalMatrix(cMatrix)
            invalidate()
        }
        animate.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        rectF.set(rLeft, rTop, rLeft + rectWidth, rTop + rectHeight)
        if (null == shader) {
            shader = LinearGradient(paddingH, 0f, paddingH + rectWidth, 0f, Color.RED, Color.GREEN, Shader.TileMode.CLAMP)
//            shader = LinearGradient(0f, 0f, rectF.width(), 0f, intArrayOf(Color.RED, Color.GREEN, Color.BLUE), floatArrayOf(0f, 0.5F, 1F), Shader.TileMode.CLAMP)
            rectPaint.shader = shader
            shader!!.setLocalMatrix(cMatrix)
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