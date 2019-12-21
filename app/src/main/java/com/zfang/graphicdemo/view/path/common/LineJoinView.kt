package com.zfang.graphicdemo.view.path.common

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.zfang.graphicdemo.common.px2Dp

/**
 * 定义线连接处的形状
 * Paint.Join.MITER 连接处没有做处理
 * Paint.Join.ROUND 连接处为圆形
 * Paint.Join.BEVEL 连接处为斜角
 *
 */
class LineJoinView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val TAG = "LineCapView"
    private var canvasHeight = 0
    private var canvasWidth = 0
    private var linePaint = Paint()
    private var redLinePaint = Paint()
    private var textPaint = TextPaint()
    private var dashLinePaint = Paint()
    private var textBound = Rect()
    private var pathBound = RectF()
    private var path = Path()

    private var pathMatrix = Matrix()
    private var pathEffect: PathEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)

    init {
        linePaint.strokeWidth = 16.px2Dp(context!!).toFloat()
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeJoin = Paint.Join.ROUND

        redLinePaint.color = Color.RED
        redLinePaint.strokeWidth = 16.px2Dp(context).toFloat()
        redLinePaint.style = Paint.Style.STROKE
        redLinePaint.strokeJoin = Paint.Join.MITER

        textPaint.color = Color.RED
        textPaint.textSize = 14.px2Dp(context).toFloat()

        dashLinePaint.color = Color.RED
        dashLinePaint.strokeWidth = 1.px2Dp(context).toFloat()
        dashLinePaint.style = Paint.Style.STROKE

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //画线时，线宽会影响绘制结果，需要把线宽所占大小也考虑进去，否则达不到想要的效果。
        var lineY = paddingTop.toFloat() + linePaint.strokeWidth / 2
        val lineLength = 100.px2Dp(context).toFloat()
        val x1 = paddingStart.toFloat()
        val x2 = paddingStart + lineLength
        val paddingVertical = 6.px2Dp(context).toFloat()
        val paddingHorizontal = 12.px2Dp(context).toFloat()
        var rectLeft = x1 - linePaint.strokeWidth / 2
        var rectTop = paddingTop.toFloat()
        var rectRight = x2 + linePaint.strokeWidth / 2
        var rectBottom = lineY

        //分开两次用同一个paint来画设置join是没效果的。必须使用path对象来画才有效果
        canvas!!.drawLine(x1, lineY, x2, lineY, linePaint)
        canvas.drawLine(x2, lineY - linePaint.strokeWidth / 2, x2, lineY + lineLength, linePaint)

        path.reset()
        path.moveTo(x1, lineY + lineLength + paddingVertical + redLinePaint.strokeWidth / 2)
        path.lineTo(x2, lineY + lineLength + paddingVertical + redLinePaint.strokeWidth / 2)
        path.lineTo(x2, lineY + lineLength * 2)
        path.lineTo(x1, lineY + lineLength * 2)

        pathMatrix.reset()
        val saveCount = canvas.saveLayer(0f, 0f, canvasWidth.toFloat(), canvasHeight.toFloat(), redLinePaint)
        redLinePaint.flags = LAYER_TYPE_SOFTWARE
        pathMatrix.postTranslate(0f, lineLength + redLinePaint.strokeWidth / 2 + paddingVertical)
        for (join in Paint.Join.values()) {
            redLinePaint.strokeJoin = join
            canvas.drawPath(path, redLinePaint)

            path.computeBounds(pathBound, true)
            val joinStr = join.name
            textPaint.getTextBounds(joinStr, 0, joinStr.length, textBound)
            val textY = (pathBound.top + pathBound.bottom) / 2 + textBound.height() / 2
            canvas.drawText(joinStr, pathBound.right + paddingHorizontal, textY, textPaint)
            val x2 = 0f + pathBound.right + paddingHorizontal + textBound.width() + paddingHorizontal
            val lineY = (pathBound.top + pathBound.bottom) / 2;
            dashLinePaint.pathEffect = pathEffect
            canvas.drawLine(0f, lineY, x2, lineY, dashLinePaint)
            path.transform(pathMatrix)
        }
        canvas.restoreToCount(saveCount)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        canvasWidth = MeasureSpec.getSize(widthMeasureSpec)
        canvasHeight = MeasureSpec.getSize(heightMeasureSpec)
        Log.e(TAG, "canvasWidth = $canvasWidth, canvasHeight = $canvasHeight")
    }
}