package com.zfang.graphicdemo.view.surface

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.zfang.graphicdemo.common.px2Dp


/**
 * 红包雨任务进度条
 */
class RedPacketProgressView2(val ctx: Context, attributeSet: AttributeSet): View(ctx, attributeSet) {

    private val TAG = "RedPacketProgressView"

    private var canvasWidth = 0f
    private var canvasHeight = 0f
    private var fgProgressHeight = 0f
    private var radius = 0f
    private var bgRectF: RectF = RectF()
    private var bgPaint = Paint()

    private var fgRectF = RectF()
    private var halfCircleRect = RectF()
    private var fgPaint = Paint()
    private var progressShader: LinearGradient? = null
    private var mGradientMatrix: Matrix = Matrix()
    private var mTranslate = 0f
    private var currentProgress = 0f
    private var outCircleRadius = 0f
    private var centerCircleRadius = 0f
    private var circlePivot = 0f
    private var circlePaint = Paint()
    private val pathPaint = Paint()
    private var path = Path()


    init {
        bgPaint.color = Color.parseColor("#66ffccec")
        circlePaint.isAntiAlias = true
        pathPaint.color = Color.parseColor("#FF69A5")
        pathPaint.strokeWidth = 2.px2Dp(ctx).toFloat()
        pathPaint.style = Paint.Style.STROKE
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        postDelayed({startRainProgress()}, 2000)
    }

    fun startRainProgress() {
        val progressAnimator = ValueAnimator.ofFloat(0f, 1f)
        progressAnimator.duration = 8000
        progressAnimator.interpolator = LinearInterpolator()
        progressAnimator.addUpdateListener { animator ->
            currentProgress = animator.animatedFraction * canvasWidth
            fgRectF.set(0f, (canvasHeight - fgProgressHeight) / 2, currentProgress, (canvasHeight / 2 + fgProgressHeight / 2))
            mTranslate = currentProgress
            if (circlePivot < currentProgress) {
                circlePivot = currentProgress
            }

            if ((circlePivot + outCircleRadius) >= canvasWidth) {
                circlePivot = canvasWidth - outCircleRadius
            }
            halfCircleRect.set(circlePivot - outCircleRadius, 0f, circlePivot + outCircleRadius, canvasHeight)

            path.reset()
            var startX = circlePivot - outCircleRadius - outCircleRadius * 1.5f
            if (startX < 0f) {
                startX = 0f
            }
            path.moveTo(startX, (canvasHeight - fgProgressHeight) / 2)
            path.quadTo(circlePivot, canvasHeight / 2, circlePivot, 0f)

//            if (progressShader != null) {
//                mGradientMatrix.setTranslate(mTranslate, 0f)
//                progressShader?.setLocalMatrix(mGradientMatrix)
//            }
            postInvalidate()
        }
        progressAnimator.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(Color.parseColor("#678cff"))
//        canvas?.drawRoundRect(bgRectF, radius, radius, bgPaint)
        canvas?.drawRoundRect(fgRectF, radius, radius, fgPaint)

        circlePaint.color = Color.parseColor("#FF3435")
//        canvas?.drawCircle(circlePivot, canvasHeight / 2, outCircleRadius, circlePaint)//外圆
        canvas?.drawArc(halfCircleRect, -135f, 270f, true, circlePaint)
        circlePaint.color = Color.parseColor("#FFFFFF")//白色
        canvas?.drawCircle(circlePivot, canvasHeight / 2, centerCircleRadius, circlePaint)
        canvas?.drawPath(path, pathPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        canvasWidth = measuredWidth.toFloat()
        canvasHeight = measuredHeight.toFloat()
        if (canvasWidth > 0) {
            radius = (canvasHeight / 2)
            outCircleRadius = radius
            centerCircleRadius = outCircleRadius / 2f
            bgRectF.set(0f, 0f, canvasWidth, canvasHeight)
            circlePivot = outCircleRadius
            val scale = 0.6f
            fgProgressHeight = scale * canvasHeight
            fgRectF.set(0f, (canvasHeight - fgProgressHeight) / 2, (canvasWidth / 2), (canvasHeight / 2 + fgProgressHeight / 2))
            path.reset()
            path.moveTo(0f, (canvasHeight - fgProgressHeight) / 2)
            path.quadTo((0f + outCircleRadius) / 2, (canvasHeight - fgProgressHeight) / 2, circlePivot, 0f)
//            path.close()

            if (null == progressShader) {
                val colors = IntArray(3)
                colors[0] = Color.parseColor("#BA90FF")
                colors[1] = Color.parseColor("#FF86E3")
                colors[2] = Color.parseColor("#FF3333")
                val positions = FloatArray(3)
                positions[0] = 0f
                positions[1] = 0.5f
                positions[2] = 1f
                progressShader = LinearGradient(0F, 0F, canvasWidth, 0f, colors, positions, Shader.TileMode.CLAMP)
                fgPaint.shader = progressShader
            }
        }

        Log.e(TAG, "onMeasure, canvasWidth = $canvasWidth, canvasHeight = $canvasHeight")
    }
}