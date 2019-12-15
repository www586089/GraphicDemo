package com.zfang.graphicdemo.view.matrix

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zfang.graphicdemo.R

class BasicMatrixView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

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
        canvas!!.drawBitmap(bitmap, bitmapMatrix, bitmapPaint)

        bitmapMatrix.reset()
        canvas.drawBitmap(bitmap, bitmapMatrix, bitmapPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (measuredWidth > 0) {
            canvasWidth = measuredWidth
            canvasHeight = measuredHeight
            //重置为单位矩阵，否则后面的计算将会无效
            bitmapMatrix.reset()
            /**
             * 把绘制坐标移动到响应计算出来的点使得绘制出来的图像是整体居中显示的
             * setTranslate也可以移动中心
             */
            bitmapMatrix.postTranslate(((canvasWidth - bitmapWidth) / 2).toFloat(), ((canvasHeight - bitmapHeight) / 2).toFloat())
        }
    }
}