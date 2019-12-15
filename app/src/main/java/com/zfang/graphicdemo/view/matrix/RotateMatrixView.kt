package com.zfang.graphicdemo.view.matrix

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zfang.graphicdemo.R

class RotateMatrixView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

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

//        canvas.save()
//        canvas.translate((canvasWidth / 2 - bitmapWidth / 2).toFloat(), (canvasHeight / 2 - bitmapHeight / 2).toFloat())
        //1 平移
        bitmapMatrix.postTranslate((canvasWidth / 2 - bitmapWidth / 2).toFloat(), (canvasHeight / 2 - bitmapHeight / 2).toFloat())
        //2 旋转， 这里的旋转中轴坐标还是相对于画布的原点进行的旋转（这里就是左上角为原点，绕view中心旋转）
        //方法一设定旋转角度
        bitmapMatrix.postRotate(90f, (canvasWidth / 2).toFloat(), (canvasHeight / 2).toFloat())
        //方法二直接操作矩阵，设置相应的值
//        bitmapMatrix.setSinCos(Math.sin(90.0).toFloat(), Math.cos(90.0).toFloat(), 0f, 0f)
        canvas.drawBitmap(bitmap, bitmapMatrix, bitmapPaint!!)
//        canvas.restore()

        canvas.save()
        bitmapPaint!!.alpha = 0x77
        canvas.translate((canvasWidth / 2).toFloat(), (canvasHeight / 2).toFloat())
        canvas.drawCircle(0f, 0f, (canvasWidth / 16).toFloat(), bitmapPaint!!)
        canvas.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (measuredWidth > 0) {
            canvasWidth = measuredWidth
            canvasHeight = measuredHeight
        }
    }
}