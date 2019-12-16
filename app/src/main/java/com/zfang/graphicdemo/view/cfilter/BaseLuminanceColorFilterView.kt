package com.zfang.graphicdemo.view.cfilter

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.zfang.graphicdemo.R

abstract class BaseLuminanceColorFilterView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var TAG = "ColorFilterView"
    private var paintList: ArrayList<Paint> = ArrayList()
    private var bitmap: Bitmap = BitmapFactory.decodeResource(context!!.resources, R.drawable.filter_icon_film2)
    private var canvasWidth: Float = 0f
    private var canvasHeight: Float = 0f
    private var bitmapWidth = 0f
    private var bitmapHeight = 0f
    private var drawCount = 4
    private var bitmapPadding = 0f

    init {
        bitmapWidth = bitmap.width.toFloat()
        bitmapHeight = bitmap.height.toFloat()
    }
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        Log.e(TAG, "width = ${bitmap.width}, height = ${bitmap.height}")

        for (index in 1..24) {
            val left = index % 4 * bitmapPadding + (index % 4 - 1) * bitmapWidth
            val top = bitmapPadding * (1 + (index - 1) / (4))
            canvas!!.drawBitmap(bitmap, left, top, paintList.get(index - 1))
        }
        canvas!!.drawColor(Color.parseColor("#3367c8ff"))
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(measuredWidth, MeasureSpec.makeMeasureSpec((bitmapHeight + 2 * bitmapPadding).toInt(), MeasureSpec.EXACTLY))
        if (measuredWidth > 0 && paintList.size <= 0) {
            canvasWidth = measuredWidth.toFloat()
            canvasHeight = measuredHeight.toFloat()
            bitmapPadding = (canvasWidth - drawCount * bitmapWidth) / (drawCount + 1)
            initPaint()
        }
    }

    fun initPaint() {
        val colorMatrixList = getColorMatrix()
        for (index in 0..23) {
            val paint = Paint()
            paint.isAntiAlias = true
            paint.setColorFilter(colorMatrixList.get(index))

            paintList.add(paint)
        }
    }

    abstract fun getColorMatrix(): ArrayList<ColorMatrixColorFilter>
}