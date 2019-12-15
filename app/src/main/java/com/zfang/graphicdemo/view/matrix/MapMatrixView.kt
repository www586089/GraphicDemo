package com.zfang.graphicdemo.view.matrix

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.common.dec2
import com.zfang.graphicdemo.common.px2Dp
import kotlin.math.log

class MapMatrixView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val TAG = "MapMatrixView"
    private var bitmap: Bitmap = BitmapFactory.decodeResource(context!!.resources,
        R.drawable.home_car_instalment
    )

    private var savedMatrix: Matrix = Matrix()
    private var bitmapMatrix: Matrix = Matrix()
    private var bitmapPaint: Paint? = null
    private val textPaint = TextPaint()
    private val circlePaint = Paint()
    private var canvasWidth: Int = 0
    private var canvasHeight: Int = 0
    private var bitmapWidth = 0
    private var bitmapHeight = 0
    private var dPoint: PointF = PointF()
    private var mPoint: PointF = PointF()
    private var mapPoint: PointF = PointF()
    private var invertMapPoint: PointF = PointF()
    private var rect: Rect = Rect()

    init {
        bitmapPaint = Paint()
        textPaint.textSize = 16.px2Dp(getContext()).toFloat()
        textPaint.color = Color.RED
        bitmapWidth = bitmap.width
        bitmapHeight = bitmap.height
        circlePaint.alpha = 0x44
        circlePaint.color = Color.RED
        circlePaint.style = Paint.Style.STROKE
        circlePaint.pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
        circlePaint.strokeWidth = 1.px2Dp(getContext()).toFloat()
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        bitmapMatrix.reset()
        canvas!!.drawBitmap(bitmap, bitmapMatrix, bitmapPaint)

        //重置为单位矩阵，否则后面的计算将会无效
        bitmapMatrix.reset()
        /**
         * 把绘制坐标移动到响应计算出来的点使得绘制出来的图像是整体居中显示的
         * setTranslate也可以移动中心
         */
        val dx = ((canvasWidth - bitmapWidth) / 2).toFloat()
        val dy = ((canvasHeight - bitmapHeight) / 2).toFloat()
        Log.e(TAG, "dx = $dx, dy = $dy")
        bitmapMatrix.postTranslate(dx, dy)
        canvas.drawBitmap(bitmap, bitmapMatrix, bitmapPaint)
        canvas.drawCircle(dx, dy, 20.px2Dp(context).toFloat(), circlePaint)


        val translateStr = "Translate x = ${dx.dec2()}, y = ${dy.dec2()}"
        textPaint.getTextBounds(translateStr, 0, translateStr.length, rect)
        var paintX = 6.px2Dp(context).toFloat()
        var paintY = rect.height().toFloat() + bitmapHeight
        canvas.drawText(translateStr, paintX, paintY, textPaint)

        val originPointStr = "Origin x = ${mPoint.x.dec2()}, y = ${mPoint.y.dec2()}"
        textPaint.getTextBounds(originPointStr, 0, originPointStr.length, rect)
        paintY += rect.height().toFloat()
        canvas.drawText(originPointStr, paintX, paintY, textPaint)

        val mapPointStr = "mapPoint x = ${mapPoint.x.dec2()}, y = ${mapPoint.y.dec2()}"
        textPaint.getTextBounds(mapPointStr, 0, mapPointStr.length, rect)
        paintY += rect.height().toFloat()
        canvas.drawText(mapPointStr, paintX, paintY, textPaint)

        val invertMapPointStr = "invertMapPointStr x = ${invertMapPoint.x.dec2()}, y = ${invertMapPoint.y.dec2()}"
        textPaint.getTextBounds(invertMapPointStr, 0, invertMapPointStr.length, rect)
        paintY += rect.height().toFloat()
        canvas.drawText(invertMapPointStr, paintX, paintY, textPaint)
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (measuredWidth > 0) {
            canvasWidth = measuredWidth
            canvasHeight = measuredHeight
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                dPoint.set(event.x, event.y)
                doMap(event)
                doInvert(event)
                refresh()
            }
            MotionEvent.ACTION_MOVE -> {
                doMap(event)
                doInvert(event)
                refresh()
            }
        }
        return true
    }


    //映射矩阵坐标点
    private fun doMap(event: MotionEvent?) {
        mPoint.set(event!!.x, event.y)
        val src = floatArrayOf(mPoint.x, mPoint.y)
        val dst = FloatArray(2)
        bitmapMatrix.mapPoints(dst, src)
        mapPoint.set(dst[0], dst[1])
    }

    //反转矩阵并映射坐标点，相当于doMap的逆向运算
    private fun doInvert(event: MotionEvent) {
        val src = floatArrayOf(mPoint.x, mPoint.y)
        val dst = FloatArray(2)
        savedMatrix.set(bitmapMatrix)
        savedMatrix.invert(savedMatrix)
        savedMatrix.mapPoints(dst, src)
        invertMapPoint.set(dst[0], dst[1])
    }

    private fun refresh() {
        invalidate()
    }
}