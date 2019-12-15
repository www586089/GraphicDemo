package com.zfang.graphicdemo.view.matrix

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zfang.graphicdemo.R

class ScaleMatrixView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

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
        //在没有平移之前默认在左上角绘制
        bitmapMatrix.reset()
        canvas!!.drawBitmap(bitmap, bitmapMatrix, bitmapPaint)

        //1 平移
        bitmapMatrix.postTranslate((canvasWidth / 2).toFloat(), (canvasHeight / 2).toFloat())
        //没有缩放之前在原位置画一个矩形便于对比
        canvas.save()
        canvas.translate((canvasWidth / 2).toFloat(), (canvasHeight / 2).toFloat())
        bitmapPaint!!.alpha = 0x77
        canvas.drawRect(0F, 0F, bitmapWidth.toFloat(), bitmapHeight.toFloat(), bitmapPaint!!)
        canvas.restore()
        //2 缩放
        bitmapMatrix.postScale(0.5f, 0.5f, (canvasWidth / 2).toFloat(), (canvasHeight / 2).toFloat())
        canvas.drawBitmap(bitmap, bitmapMatrix, bitmapPaint!!)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (measuredWidth > 0) {
            canvasWidth = measuredWidth
            canvasHeight = measuredHeight
        }
    }
}