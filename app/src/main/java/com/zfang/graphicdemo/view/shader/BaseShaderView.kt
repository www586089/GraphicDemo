package com.zfang.graphicdemo.view.shader

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.zfang.graphicdemo.common.px2Dp

abstract class BaseShaderView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var canvasHeight = 0f
    private var canvasWidth = 0f
    private var rectPaint = Paint()
    private var dashPaint = Paint()
    private var rectF = RectF()
    private var shader: Shader? = null
    private var pathShader:Shader? = null

    private var circlePaint = Paint()
    private var shaderCircle: Shader? = null
    private var pathPaint = Paint()


    val paddingH = 12.px2Dp(context!!).toFloat()
    val paddingV = 6.px2Dp(context!!).toFloat()
    var rectWidth = 0f
    var rectHeight = 0f
    val rLeft = paddingH
    val rTop = paddingV

    val bottomPath = Path()

    init {
        rectPaint.style = Paint.Style.FILL
        rectPaint.color = Color.RED

        circlePaint.style = Paint.Style.FILL

        dashPaint.pathEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        dashPaint.strokeWidth = 1.px2Dp(context!!).toFloat()
        dashPaint.style = Paint.Style.STROKE
        dashPaint.color = Color.BLACK

        pathPaint.style = Paint.Style.FILL_AND_STROKE
        pathPaint.strokeWidth = 2.px2Dp(context).toFloat()
        pathPaint.color = Color.RED
        pathPaint.isAntiAlias = true
        pathPaint.strokeJoin = Paint.Join.ROUND
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        rectF.set(rLeft, rTop, rLeft + rectWidth, rTop + rectHeight)
        if (null == shader) {
            shader = getShader(paddingH, rTop, paddingH + rectWidth, rTop + rectHeight)
            rectPaint.shader = shader
        }
        canvas!!.drawRect(rectF, rectPaint)
        canvas.drawRect(rectF, dashPaint)


        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2
        val radius = canvasWidth / 4
        if (null == shaderCircle) {
            val cLeft = centerX - radius
            val cTop = centerY - radius
            val cRight = centerX + radius
            val cBottom = centerY + radius
            shaderCircle = getShaderCircle(cLeft, cTop, cRight, cBottom)
            circlePaint.shader = shaderCircle
        }

        canvas.drawCircle(centerX, centerY, radius, circlePaint)


        bottomPath.reset()
        val pLeft = paddingH
        val pTop = centerY + radius + paddingV
        val pRight = canvasWidth - paddingH
        val pBottom = canvasHeight - paddingV
        val r = 5.px2Dp(context).toFloat()
        canvas.drawRoundRect(pLeft, pTop, pRight, pBottom, r, r, dashPaint)

        bottomPath.moveTo(pLeft, (pTop + pBottom) / 2)
        bottomPath.quadTo((pLeft + (pRight - pLeft) / 4), pTop + (pBottom - pTop) / 4, (pLeft + pRight) / 2, pTop)
        bottomPath.quadTo(pRight - (pRight - pLeft) / 4, pTop + (pBottom - pTop) / 4 , pRight, (pTop + pBottom) / 2)

        bottomPath.quadTo(pRight - (pRight - pLeft) / 4, pBottom - (pBottom - pTop) / 4, (pLeft + pRight) / 2, pBottom)
        bottomPath.quadTo(pLeft + (pRight - pLeft) / 4, pBottom - (pBottom - pTop) / 4, pLeft, (pTop + pBottom) / 2)

        if (null == pathShader) {
            pathShader = getShaderPath(pLeft, pTop, pRight, pBottom)
            pathPaint.shader = pathShader
        }
        canvas.drawPath(bottomPath, pathPaint)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        canvasWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        canvasHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()

        rectWidth = canvasWidth / 2
        rectHeight = rectWidth / 3
    }

    abstract fun getShader(left: Float, top: Float, right: Float, bottom: Float): Shader
    abstract fun getShaderCircle(left: Float, top: Float, right: Float, bottom: Float): Shader

    abstract fun getShaderPath(left: Float, top: Float, right: Float, bottom: Float): Shader
}