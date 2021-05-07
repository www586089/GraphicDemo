package com.zfang.graphicdemo.view.surface

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.zfang.graphicdemo.common.px2Dp

class RedPacketProgressView(val ctx: Context, attributeSet: AttributeSet): View(ctx, attributeSet) {


    private var canvasWidth = 0f
    private var canvasHeight = 0f
    private val fgPaint = Paint()
    private val bgPaint = Paint()
    private var outCircleRadius = 0f    //大圆半径
    private var littleCircleRadius = 0f //小圆半径
    private var rectF = RectF()
    private var outCircleRectF = RectF()
    private val path = Path()
    private var pointShader: Shader? = null
    private var pointMatrix: Matrix? = null
    private var bgShader: Shader? = null
    private var scaleRatio = 2f

    init {
        bgPaint.color = Color.BLUE
        bgPaint.strokeWidth = 1.px2Dp(ctx).toFloat()

        fgPaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawColor(Color.parseColor("#67c8ff"))
            it.drawRoundRect(rectF, littleCircleRadius, littleCircleRadius, bgPaint)
            it.drawPath(path, fgPaint)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        postDelayed({ startAnimation() }, 2000)
    }

    private fun startAnimation() {
        val progressAnimator = ValueAnimator.ofFloat(0f, 1f)
        progressAnimator.duration = 8000
        progressAnimator.interpolator = AccelerateDecelerateInterpolator()
        progressAnimator.addUpdateListener { animator ->
            var centerX = animator.animatedFraction * canvasWidth
            val centerY = canvasHeight / 2
            if (centerX < outCircleRadius * scaleRatio) {
                centerX = outCircleRadius * scaleRatio
            }
            if (centerX + outCircleRadius > canvasWidth) {
                centerX = canvasWidth - outCircleRadius
            }
            rectF.set(0f, centerY - littleCircleRadius, centerX, centerY + littleCircleRadius)
            buildPathCubic(centerX, centerY, 0f, 2f)
            postInvalidate()
        }
        progressAnimator.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        canvasWidth = measuredWidth.toFloat()
        canvasHeight = measuredHeight.toFloat()

        if (canvasHeight > 0) {
            outCircleRadius = canvasHeight / 4
            littleCircleRadius = 0.6f * outCircleRadius
            val centerY = canvasHeight / 2
            val centerX = 0.45f * canvasWidth

            rectF.set(0f, centerY - littleCircleRadius, centerX, centerY + littleCircleRadius)
            outCircleRectF.set(centerX - outCircleRadius, centerY - outCircleRadius, centerX + outCircleRadius, centerY + outCircleRadius)
            buildPathCubic(centerX, centerY, 10f, scaleRatio)

            if (null == bgShader) {
                val colors = IntArray(3)
                colors[0] = Color.parseColor("#BA90FF")
                colors[1] = Color.parseColor("#FF86E3")
                colors[2] = Color.parseColor("#FF3333")
                val positions = FloatArray(3)
                positions[0] = 0f
                positions[1] = 0.5f
                positions[2] = 1f
                bgShader = LinearGradient(0F, 0F, canvasWidth, 0f, colors, positions, Shader.TileMode.CLAMP)
                bgPaint.shader = bgShader

                pointMatrix = Matrix()
                val startX = centerX - scaleRatio * outCircleRadius
                val startY = centerY - outCircleRadius
                val endX = centerX + outCircleRadius
                val endY = centerY + outCircleRadius

                val colorsPoint = IntArray(2)
                colorsPoint[0] = Color.parseColor("#FF3333")
                colorsPoint[1] = Color.parseColor("#FF3435")
                val positionsPoint = FloatArray(2)
                positionsPoint[0] = 0f
                positionsPoint[1] = 1f
                pointShader = LinearGradient(startX, startY, endX, endY, colorsPoint, positionsPoint, Shader.TileMode.CLAMP)
                fgPaint.shader = bgShader
            }
        }
    }


    /**
     *
     */
    private fun buildPathQuad(centerX: Float, centerY: Float, angle: Float, scaleRatio: Float) {
        path.rewind()
        outCircleRectF.set(centerX - outCircleRadius, centerY - outCircleRadius, centerX + outCircleRadius, centerY + outCircleRadius)
        val startX = centerX - scaleRatio * outCircleRadius
        val startY = centerY - littleCircleRadius
        path.moveTo(startX, startY)
        val x1 = centerX - Math.sqrt(Math.pow(outCircleRadius.toDouble(), 2.0) - Math.pow(littleCircleRadius.toDouble(), 2.0)).toFloat()
        val y1 = centerY - littleCircleRadius
        val pi45 = (angle / 180.0) * Math.PI
        val x2 = centerX - (Math.sin(pi45) * outCircleRadius).toFloat()
        val y2 = centerY - (Math.cos(pi45) * outCircleRadius).toFloat()
        path.quadTo(x1, y1, x2, y2)
        path.addArc(outCircleRectF, -(angle + 90), 180f + 2 * angle)
        path.quadTo(x1, centerY + littleCircleRadius, centerX - scaleRatio * outCircleRadius, centerY + littleCircleRadius)
        path.lineTo(startX, startY)
    }

    private fun buildPathCubic(centerX: Float, centerY: Float, angle: Float, scaleRatio: Float) {
        path.rewind()
        outCircleRectF.set(centerX - outCircleRadius, centerY - outCircleRadius, centerX + outCircleRadius, centerY + outCircleRadius)
        var startX = centerX - scaleRatio * outCircleRadius
        if (startX < littleCircleRadius) {
            startX = littleCircleRadius
        }
        val startY = centerY - littleCircleRadius
        path.moveTo(startX, startY)
        val x1 = centerX - Math.sqrt(Math.pow(outCircleRadius.toDouble(), 2.0) - Math.pow(littleCircleRadius.toDouble(), 2.0)).toFloat()
        val y1 = centerY - littleCircleRadius
        val pi45 = (angle / 180.0) * Math.PI

        val x3 = centerX - (Math.sin(pi45) * outCircleRadius).toFloat()
        val y3 = centerY - (Math.cos(pi45) * outCircleRadius).toFloat()
        val x2 = (x1 + x3) / 2
        val y2 = y3
        path.cubicTo(x1, y1, x2, y2, x3, y3)
        path.addArc(outCircleRectF, -(angle + 90), 180f + 2 * angle)
        path.cubicTo(x2, centerY + outCircleRadius, x1, centerY + littleCircleRadius, startX, centerY + littleCircleRadius)
        path.lineTo(startX, startY)
    }
}