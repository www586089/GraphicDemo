package com.zfang.graphicdemo.view.matrix

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.common.px2Dp

class RotateMatrixView(val ctx: Context, attrs: AttributeSet?) : View(ctx, attrs) {

    private var bitmap: Bitmap = BitmapFactory.decodeResource(context.resources,
        R.drawable.home_car_instalment
    )
    private var bitmapMatrix: Matrix = Matrix()
    private var bitmapPaint: Paint = Paint()
    private var canvasWidth: Int = 0
    private var canvasHeight: Int = 0
    private var bitmapWidth = 0f
    private var bitmapHeight = 0f
    private var rectF: RectF = RectF()
    private var path = Path()
    private val region = Region()
    private val regionRect = Region()

    init {
        bitmapPaint.color = Color.RED
        bitmapPaint.style = Paint.Style.STROKE
        bitmapPaint.strokeWidth = 2.px2Dp(ctx).toFloat()
        bitmapWidth = bitmap.width.toFloat()
        bitmapHeight = bitmap.height.toFloat()

        val offset = 5f
        rectF.set(-offset, -offset, bitmapWidth + offset, bitmapHeight + offset)

        path.moveTo(-offset, -offset)
        path.lineTo(bitmapWidth + offset, -offset)
        path.lineTo(bitmapWidth + offset, bitmapHeight + offset)
        path.lineTo(-offset, bitmapHeight + offset)
        path.lineTo(-offset, -offset)
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //在没有平移之前默认在左上角绘制
        bitmapMatrix.reset()
        canvas!!.drawBitmap(bitmap, bitmapMatrix, bitmapPaint)

//        canvas.save()
//        canvas.translate((canvasWidth / 2 - bitmapWidth / 2).toFloat(), (canvasHeight / 2 - bitmapHeight / 2).toFloat())
        //1 平移
        bitmapMatrix.postTranslate((canvasWidth / 2 - bitmapWidth / 2), (canvasHeight / 2 - bitmapHeight / 2))
        canvas.drawBitmap(bitmap, bitmapMatrix, bitmapPaint)



//        bitmapMatrix.mapRect(rectF)
//        canvas.save()
//        canvas.rotate(30f, (canvasWidth / 2).toFloat(), (canvasHeight / 2).toFloat())
//        canvas.drawRect(rectF, bitmapPaint)
//        canvas.restore()
//        canvas.drawRect(rectF, bitmapPaint)
        //2 旋转， 这里的旋转中轴坐标还是相对于画布的原点进行的旋转（这里就是左上角为原点，绕view中心旋转）
        //方法一设定旋转角度
        bitmapMatrix.postRotate(30f, (canvasWidth / 2).toFloat(), (canvasHeight / 2).toFloat())
        path.transform(bitmapMatrix)
        canvas.drawPath(path, bitmapPaint)
//        bitmapMatrix.mapRect(rectF)
//        canvas.drawRect(rectF, bitmapPaint)
        //方法二直接操作矩阵，设置相应的值
//        bitmapMatrix.setSinCos(Math.sin(90.0).toFloat(), Math.cos(90.0).toFloat(), 0f, 0f)
        canvas.drawBitmap(bitmap, bitmapMatrix, bitmapPaint)

//        canvas.restore()

        canvas.save()
        bitmapPaint.alpha = 0x77
        canvas.translate((canvasWidth / 2).toFloat(), (canvasHeight / 2).toFloat())
        canvas.drawCircle(0f, 0f, (canvasWidth / 16).toFloat(), bitmapPaint)
        canvas.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (measuredWidth > 0) {
            canvasWidth = measuredWidth
            canvasHeight = measuredHeight
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            path.computeBounds(rectF, true)
            regionRect.set(rectF.left.toInt(), rectF.top.toInt(), rectF.right.toInt(), rectF.bottom.toInt())
            region.setPath(path, regionRect)
            Log.e("zfang", "contains = ${region.contains(event.x.toInt(), event.y.toInt())}")
        }
        return super.onTouchEvent(event)
    }
}