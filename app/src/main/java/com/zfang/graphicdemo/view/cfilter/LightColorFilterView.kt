package com.zfang.graphicdemo.view.cfilter

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.zfang.graphicdemo.R

/**
 * 光线颜色过滤
 */
class LightColorFilterView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {

    private var TAG = "LightColorFilterView"
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

        var left: Float
        var top: Float
        for (index in 0..7) {
            if (0 == (index + 1) % drawCount) {
                left = drawCount * bitmapPadding + (drawCount - 1) * bitmapWidth
            } else {
                left = bitmapPadding * ((index + 1) % drawCount) + (index % drawCount) * bitmapWidth
            }

            if (index < drawCount) {
                top = bitmapPadding
            } else {
                top = bitmapPadding * ((index) / drawCount + 1) + (index / drawCount) * bitmapHeight
            }

            canvas!!.drawBitmap(bitmap, left, top, paintList.get(index))

        }
        canvas!!.drawColor(Color.parseColor("#3367c8ff"))
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (measuredWidth > 0 && paintList.size <= 0) {
            canvasWidth = measuredWidth.toFloat()
            canvasHeight = measuredHeight.toFloat()
            bitmapPadding = (canvasWidth - drawCount * bitmapWidth) / (drawCount + 1)
            initPaint()
        }
    }

    fun initPaint() {
        val colorMatrixList = getColorFilter()
        for (index in 0..7) {
            val paint = Paint()
            paint.isAntiAlias = true
            paint.setColorFilter(colorMatrixList.get(index))

            paintList.add(paint)
        }
    }

    fun getColorFilter(): ArrayList<LightingColorFilter> {
        var lightColorFilters = ArrayList<LightingColorFilter>(8)

        //不变
        var cFilter0 = LightingColorFilter(0xFFFFFF, 0X000000)

        //去掉红色
        var cFilter1 = LightingColorFilter(0x00FFFF, 0X000000)

        //去掉绿色
        var cFilter2 = LightingColorFilter(0xFF00FF, 0X000000)

        //去掉蓝色
        var cFilter3 = LightingColorFilter(0xFFFF00, 0X000000)

        //增加红色
        var cFilter4 = LightingColorFilter(0xFFFFFF, 0X560000)

        //增加绿色
        var cFilter5 = LightingColorFilter(0xFFFFFF, 0X006400)

        //增加蓝色
        var cFilter6 = LightingColorFilter(0xFFFFFF, 0X000056)

        //增加蓝绿色
        var cFilter7 = LightingColorFilter(0xFFFFFF, 0X006456)

        lightColorFilters.add(cFilter0)
        lightColorFilters.add(cFilter1)
        lightColorFilters.add(cFilter2)
        lightColorFilters.add(cFilter3)
        lightColorFilters.add(cFilter4)
        lightColorFilters.add(cFilter5)
        lightColorFilters.add(cFilter6)
        lightColorFilters.add(cFilter7)

        return lightColorFilters
    }
}