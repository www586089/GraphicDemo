package com.zfang.graphicdemo.view.shader

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.graphics.transform
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.common.px2Dp
import kotlin.math.sqrt

/**
 * 颜色渐变----放大
 */
class ShaderScaleCircleView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val TAG = "ShaderScaleCircleView"
    private var canvasHeight = 0f
    private var canvasWidth = 0f
    private var rectPaint = Paint()
    private var dashPaint = Paint()
    private val circlePaint = Paint()
    private val pathPaint = Paint()
    private val textPaint = TextPaint()
    private var rectF = RectF()
    private var shader: Shader? = null
    private var cMatrix = Matrix()
    private var animatedRectF = RectF()
    private var originRect = RectF()
    private var taijiPathBound = RectF()


    val paddingH = 12.px2Dp(context!!).toFloat()
    val paddingV = 6.px2Dp(context!!).toFloat()
    var rectWidth = 0f
    var rectHeight = 0f
    val rLeft = paddingH
    val rTop = paddingV
    var radius = 0f
    var animatedRadius = 0f
    var downX = 0F
    var downY = 0F
    var touchSlop = 0f
    var isMoving = false
    var actionDownTime = 0L

    var centerX = 0F
    var centerY = 0F
    val taijiPath = Path()

    var bitmap = BitmapFactory.decodeResource(context!!.resources, R.drawable.filter_icon_film2)
    var bitmapCircle = BitmapFactory.decodeResource(context!!.resources, R.drawable.filter_icon_fresh2)
    var taijiShader: Shader? = null

    init {
        rectPaint.style = Paint.Style.FILL
        rectPaint.color = Color.RED

        dashPaint.pathEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        dashPaint.strokeWidth = 1.px2Dp(context!!).toFloat()
        dashPaint.style = Paint.Style.STROKE
        dashPaint.color = Color.BLACK

        pathPaint.style = Paint.Style.STROKE
        pathPaint.color = Color.RED
        pathPaint.pathEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        pathPaint.strokeWidth = 1.px2Dp(context).toFloat()
        pathPaint.isAntiAlias = true

        textPaint.color = Color.RED
        textPaint.textSize = 12.px2Dp(context).toFloat()

        circlePaint.style = Paint.Style.STROKE
        circlePaint.color = Color.BLUE

        touchSlop = ViewConfiguration.getTouchSlop().toFloat()
    }

    private fun startAnimation() {
        val animate = ValueAnimator.ofFloat(0f, radius)
        animate.setDuration(500)
        animate.addUpdateListener {
            val value = it.animatedValue as Float
            val scale = value / radius
            animatedRadius = value
            Log.e("zfang", "value = $value, scale = $scale")
            cMatrix.setScale(scale, scale ,paddingH, rectHeight / 2)

            animatedRectF.set(originRect)
            animatedRectF.transform(cMatrix)
            Log.e(TAG, "animatedRectF.width = ${animatedRectF.width()}")
            invalidate()
        }
        animate.start()
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.apply {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    isMoving = false
                    downX = event.x
                    downY = event.y

                    centerX = downX
                    centerY = downY
                    actionDownTime = System.currentTimeMillis()
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaX = Math.abs(downX - event.x)
                    val deltaY = Math.abs(downY - event.y)

                    if (sqrt(Math.pow(deltaX.toDouble(), 2.toDouble()) + Math.pow(deltaY.toDouble(), 2.0)) > touchSlop) {
                        isMoving = true
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    val delta = System.currentTimeMillis() - actionDownTime
                    if (!isMoving) {
                        if (delta < 300) {
                            createShader()
                            startAnimation()
                        }
                    }
                }
            }
        }

        return true
    }

    private fun createShader() {
//        shader = LinearGradient(centerX - radius, centerY, centerX + radius, centerY, Color.RED, Color.GREEN, Shader.TileMode.CLAMP)
//        shader = RadialGradient(centerX, centerY, radius, Color.RED, Color.BLUE, Shader.TileMode.CLAMP)
//        shader = RadialGradient(centerX, centerY, radius, intArrayOf(Color.RED, Color.BLUE, Color.RED, Color.BLUE, Color.RED), floatArrayOf(0f, 0.25f, 0.5f, 0.75f, 1f), Shader.TileMode.CLAMP)
        shader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        animatedRectF.set(paddingH, rTop, paddingH + rectWidth, rTop + rectHeight)
        originRect.set(animatedRectF)
        rectPaint.shader = shader
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        rectF.set(rLeft, rTop, rLeft + rectWidth, rTop + rectHeight)

//        centerX = (rectF.left + rectF.right) / 2
//        centerY = (rectF.top + rectF.bottom) / 2

        if (rectF.contains(centerX, centerY)) {
            val saveCount = canvas!!.saveLayer(rectF, rectPaint)
            canvas.clipRect(rectF)
            canvas.drawCircle(centerX, centerY, radius/*animatedRadius*/, rectPaint)
            canvas.restoreToCount(saveCount)
        }
        canvas!!.drawRect(rectF, dashPaint)


        drawTaiji(canvas)
    }


    private fun drawTaiji(canvas: Canvas) {
        canvas.save()
        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2
        val radius = canvasWidth / 4
        val radiusLittle = radius / 2
        if (null == taijiShader) {
            taijiShader = BitmapShader(bitmapCircle, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
//            circlePaint.shader = taijiShader
        }

        canvas.drawLine(0f, centerY, canvasWidth, centerY, circlePaint)
        canvas.drawLine(centerX, centerY - 1.5f * radius, centerX, centerY + 1.5f * radius, circlePaint)

//        canvas.drawCircle(centerX, centerY, radius, circlePaint)
//        canvas.drawCircle(centerX, centerY - radiusLittle, radiusLittle, circlePaint)
//        canvas.drawCircle(centerX - radiusLittle, centerY, radiusLittle, circlePaint)
//        canvas.drawCircle(centerX, centerY + radiusLittle, radiusLittle, circlePaint)

        taijiPath.reset()
        taijiPath.moveTo(centerX, centerY)
        taijiPath.addCircle(centerX, centerY - radiusLittle, radiusLittle, Path.Direction.CW)
        canvas.drawTextOnPath("123456", taijiPath, 0f, 0f, textPaint)


        //

        val pathOutCircle = Path()
        pathOutCircle.moveTo(centerX, centerY)
        pathOutCircle.addCircle(centerX, centerY, radius, Path.Direction.CW)


        val pathTopCircle = Path()
        pathTopCircle.moveTo(centerX, centerY)
        pathTopCircle.addCircle(centerX, centerY - radiusLittle, radiusLittle, Path.Direction.CW)

        val pathBottomCircle = Path()
        pathBottomCircle.moveTo(centerX, centerY)
        pathBottomCircle.addCircle(centerX, centerY + radiusLittle, radiusLittle, Path.Direction.CW)

        val pathRect = Path()
        pathRect.addRect(centerX, centerY - radius, centerX + radius, centerY + radius, Path.Direction.CW)

        taijiPath.reset()
        taijiPath.addPath(pathOutCircle)
        taijiPath.op(pathRect, Path.Op.DIFFERENCE)
        taijiPath.op(pathBottomCircle, Path.Op.DIFFERENCE)
        taijiPath.op(pathTopCircle, Path.Op.UNION)

        canvas.save()
        canvas.clipPath(taijiPath)
        canvas.drawPath(taijiPath, pathPaint)

        if (taijiPathBound.contains(ShaderScaleCircleView@this.centerX, ShaderScaleCircleView@this.centerY)) {
            canvas.drawCircle(ShaderScaleCircleView@this.centerX, ShaderScaleCircleView@this.centerY, animatedRadius, rectPaint)
        }
        canvas.restore()

        taijiPath.computeBounds(taijiPathBound, true)
        canvas.clipRect(taijiPathBound)
        canvas.drawRect(taijiPathBound, pathPaint)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        canvasWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        canvasHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()

        rectWidth = canvasWidth / 2
        rectHeight = rectWidth / 2

        radius = if (rectWidth > rectHeight) {
            rectWidth
        } else {
            rectHeight
        }
    }
}