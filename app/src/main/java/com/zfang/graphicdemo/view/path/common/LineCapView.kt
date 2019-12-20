package com.zfang.graphicdemo.view.path.common

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.zfang.graphicdemo.common.px2Dp

/**
 * 定义线末端的形状
 *
 */
class LineCapView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private final val TAG = "LineCapView"
    private var canvasHeight = 0
    private var canvasWidth = 0
    private var linePaint = Paint()
    private var textPaint = TextPaint()
    private var rectPaint = Paint()
    private var textBount = Rect()

    init {
        linePaint.strokeWidth = 16.px2Dp(context!!).toFloat()
        linePaint.strokeCap = Paint.Cap.BUTT

        textPaint.color = Color.RED
        textPaint.textSize = 14.px2Dp(context).toFloat()

        rectPaint.color = Color.RED
        rectPaint.strokeWidth = 1.px2Dp(context).toFloat()
        rectPaint.style = Paint.Style.STROKE
        rectPaint.pathEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //画线时，线宽会影响绘制结果，需要把线宽所占大小也考虑进去，否则达不到想要的效果。
        var lineY = paddingTop.toFloat() + linePaint.strokeWidth / 2
        val lineLength = 200.px2Dp(context).toFloat()
        val x1 = paddingStart.toFloat()
        val x2 = paddingStart + lineLength
        val paddingVertical = 6.px2Dp(context).toFloat()
        val paddingHorizontal = 12.px2Dp(context).toFloat()
        var rectLeft = x1 - linePaint.strokeWidth / 2
        var rectTop = paddingTop.toFloat()
        var rectRight = x2 + linePaint.strokeWidth / 2
        var rectBottom = lineY
        for (cap in Paint.Cap.values()) {
            linePaint.strokeCap = cap
            //绘制效果直线
            canvas!!.drawLine(x1, lineY, x2, lineY, linePaint)

            val capStr = cap.name
            //测量文字矩形大小
            textPaint.getTextBounds(capStr, 0, capStr.length, textBount)
            //绘制效果名字
            canvas.drawText(capStr, rectRight + paddingHorizontal,
                lineY + (textPaint.strokeWidth - textBount.height()) / 2 + textBount.height(), textPaint)
            lineY += linePaint.strokeWidth + paddingVertical
        }
        rectBottom = lineY - (paddingVertical + linePaint.strokeWidth / 2)
        //包裹的虚线矩形
        canvas!!.drawRect(rectLeft, rectTop, rectRight, rectBottom, rectPaint)

//        canvas!!.drawLine(100f, 100f, 200f, 400f, rectPaint)
        //左侧竖线
        canvas.drawLine(rectLeft + linePaint.strokeWidth / 2, 0f, rectLeft + linePaint.strokeWidth / 2, rectBottom + paddingVertical, rectPaint)
        //右侧竖线
        canvas.drawLine(rectRight - linePaint.strokeWidth / 2, 0f, rectRight - linePaint.strokeWidth / 2, rectBottom + paddingVertical, rectPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        canvasWidth = MeasureSpec.getSize(widthMeasureSpec)
        canvasHeight = MeasureSpec.getSize(heightMeasureSpec)
        Log.e(TAG, "canvasWidth = $canvasWidth, canvasHeight = $canvasHeight")
    }
}