package com.zfang.graphicdemo.view.matrix

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zfang.graphicdemo.R

class ClipView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var bitmap: Bitmap = BitmapFactory.decodeResource(context!!.resources,
        R.drawable.home_car_instalment
    )
    private var bitmapMatrix: Matrix = Matrix()
    private var bitmapPaint: Paint? = null
    private var rectPaint: Paint? = null
    private var canvasWidth: Int = 0
    private var canvasHeight: Int = 0
    private var bitmapWidth = 0
    private var bitmapHeight = 0

    init {
        bitmapPaint = Paint()
        rectPaint = Paint();
        rectPaint!!.style = Paint.Style.STROKE
        rectPaint!!.strokeWidth = 4F
        rectPaint!!.color = Color.RED
        rectPaint!!.pathEffect = DashPathEffect(floatArrayOf(60.0f, 20.0f), 0f)
        bitmapWidth = bitmap.width
        bitmapHeight = bitmap.height
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawBitmap(bitmap, bitmapMatrix, bitmapPaint)

        //重置为单位矩阵，否则后面的计算将会无效
        bitmapMatrix.reset()
        /**
         * 把绘制坐标移动到响应计算出来的点使得绘制出来的图像是整体居中显示的
         * setTranslate也可以移动中心
         */
        bitmapMatrix.postTranslate(((canvasWidth - bitmapWidth) / 2).toFloat(), ((canvasHeight - bitmapHeight) / 2).toFloat())

        val rLeft = ((canvasWidth - bitmapWidth) / 2).toFloat()
        val rTop = ((canvasHeight - bitmapHeight) / 2).toFloat()
        val rRight = rLeft + bitmapWidth
        val rBottom = rTop + bitmapHeight

        canvas.save()
        canvas.clipRect(rLeft + bitmapWidth / 4, rTop + bitmapHeight / 4, rLeft + 3 * bitmapWidth / 4, + rTop + 3 * bitmapHeight / 4)
        canvas.drawBitmap(bitmap, bitmapMatrix, bitmapPaint)
        bitmapMatrix.reset()
        canvas.restore()

        canvas.drawRect(rLeft, rTop, rRight, rBottom, rectPaint!!)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (measuredWidth > 0) {
            canvasWidth = measuredWidth
            canvasHeight = measuredHeight
        }
    }
}