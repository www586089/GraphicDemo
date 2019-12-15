package com.zfang.graphicdemo.view.matrix

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zfang.graphicdemo.R

class InvertMatrixView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var bitmap: Bitmap = BitmapFactory.decodeResource(context!!.resources,
        R.drawable.home_car_instalment
    )
    private var bitmapMatrix: Matrix = Matrix()
    private var bitmapPaint: Paint? = null
    private var canvasWidth: Int = 0
    private var canvasHeight: Int = 0
    private var bitmapWidth = 0
    private var bitmapHeight = 0

    init {
        bitmapPaint = Paint()
        bitmapWidth = bitmap.width
        bitmapHeight = bitmap.height
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //重置为单位矩阵，否则后面的计算将会无效
        bitmapMatrix.reset()
        /**
         * 把绘制坐标移动到响应计算出来的点使得绘制出来的图像是整体居中显示的
         * setTranslate也可以移动中心
         */
        var src = floatArrayOf(
            0f, 0f,//左上角
            0f, bitmapHeight.toFloat(),//左下角
            0f + bitmapWidth, bitmapHeight.toFloat()//右下角
        )
        val offset = ((canvasWidth - bitmapWidth) / 2).toFloat()
        var dst = floatArrayOf(
            0f, 0f,//左上角
            0f + offset, bitmapHeight.toFloat(),//左下角
            0f + bitmapWidth + offset, bitmapHeight.toFloat()//右下角
        )
        bitmapMatrix.setPolyToPoly(src, 0, dst, 0, src.size / 2)
        canvas!!.drawBitmap(bitmap, bitmapMatrix, bitmapPaint)

        bitmapMatrix.invert(bitmapMatrix)
        bitmapMatrix.preTranslate(canvasWidth - bitmapWidth.toFloat(), bitmapHeight.toFloat())
        canvas.drawBitmap(bitmap, bitmapMatrix, bitmapPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (measuredWidth > 0) {
            canvasWidth = measuredWidth
            canvasHeight = measuredHeight
        }
    }
}