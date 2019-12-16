package com.zfang.graphicdemo.view.cfilter

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.zfang.graphicdemo.R
import kotlinx.android.synthetic.main.activity_main.view.*

abstract class BaseColorFilterView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {


    private var TAG = "ColorFilterView"
    private var paint = Paint()
    private var bitmap: Bitmap = BitmapFactory.decodeResource(context!!.resources, R.drawable.filter_icon_film2)
    private var canvasWidth: Float = 0f
    private var canvasHeight: Float = 0f
    private var bitmapWidth = 0f
    private var bitmapHeight = 0f
    private var drawCount = 4
    private var bitmapPadding = 0f
    private var cFilter: ColorMatrixColorFilter? = null

    init {
        paint.isAntiAlias = true

        bitmapWidth = bitmap.width.toFloat()
        bitmapHeight = bitmap.height.toFloat()
    }
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        Log.e(TAG, "width = ${bitmap.width}, height = ${bitmap.height}")

        for (index in 1..drawCount) {
            val left = index * bitmapPadding + (index - 1) * bitmapWidth
            val top = bitmapPadding
            canvas!!.drawBitmap(bitmap, left, top, paint)
        }
        canvas!!.drawColor(Color.parseColor("#3367c8ff"))
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(measuredWidth, MeasureSpec.makeMeasureSpec((bitmapHeight + 2 * bitmapPadding).toInt(), MeasureSpec.EXACTLY))
        if (measuredWidth > 0 && null == cFilter) {
            canvasWidth = measuredWidth.toFloat()
            canvasHeight = measuredHeight.toFloat()
            bitmapPadding = (canvasWidth - drawCount * bitmapWidth) / (drawCount + 1)
            cFilter = ColorMatrixColorFilter(getColorMatrix())
            paint.colorFilter = cFilter
        }
    }

    abstract fun getColorMatrix(): FloatArray
}