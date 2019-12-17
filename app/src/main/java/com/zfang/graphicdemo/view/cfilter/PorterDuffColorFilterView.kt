package com.zfang.graphicdemo.view.cfilter

import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.common.px2Dp

class PorterDuffColorFilterView(context: Context?, attrs: AttributeSet?) :
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
    private var rect = Rect()
    private var textPaint = TextPaint()
    private var porterDuffModeStr = listOf(
        "CLEAR",    "SRC",      "DST",      "SRC_OVER",
        "DST_OVER", "SRC_IN",   "DST_IN",   "SRC_OUT",
        "DST_OUT",  "SRC_ATOP", "DST_ATOP", "XOR",
        "DARKEN",   "LIGHTEN",  "MULTIPLY", "SCREEN",
        "ADD",      "OVERLAY"
    )

    init {
        bitmapWidth = bitmap.width.toFloat()
        bitmapHeight = bitmap.height.toFloat()

        textPaint.textSize = 14.px2Dp(getContext()).toFloat()
        textPaint.color = Color.RED
    }
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        Log.e(TAG, "width = ${bitmap.width}, height = ${bitmap.height}")

        var left: Float
        var top: Float
        for (index in 0..17) {
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

            var paint = paintList.get(index)
            canvas!!.drawBitmap(bitmap, left, top, paint)

            var modeStr = porterDuffModeStr[index]
            textPaint.getTextBounds(modeStr, 0, modeStr.length, rect)
            canvas.drawText(porterDuffModeStr[index], left + (bitmapWidth - rect.width()) / 2, top + (bitmapHeight - rect.height()) / 2 + rect.height(), textPaint)
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
        for (index in 0..17) {
            val paint = Paint()
            paint.isAntiAlias = true
            paint.setColorFilter(colorMatrixList.get(index))

            paintList.add(paint)
        }
    }

    fun getColorFilter(): ArrayList<PorterDuffColorFilter> {
        var lightColorFilters = ArrayList<PorterDuffColorFilter>(8)

        //所绘制源图像不会提交到画布上
        var cFilter0 = PorterDuffColorFilter(Color.parseColor("#5567c8ff"), PorterDuff.Mode.CLEAR)

        //只显示源图像
        var cFilter1 = PorterDuffColorFilter(Color.parseColor("#5567c8ff"), PorterDuff.Mode.SRC)

        //只显示目标图像
        var cFilter2 = PorterDuffColorFilter(Color.parseColor("#5567c8ff"), PorterDuff.Mode.DST)

        //正常绘制显示，源图像剧上显示
        var cFilter3 = PorterDuffColorFilter(Color.parseColor("#5567c8ff"), PorterDuff.Mode.SRC_OVER)

        //正常绘制显示，目标图像居上显示（上下层都显示）
        var cFilter4 = PorterDuffColorFilter(Color.parseColor("#5567c8ff"), PorterDuff.Mode.DST_OVER)

        //取两层绘制交集中的源图像
        var cFilter5 = PorterDuffColorFilter(Color.parseColor("#5567c8ff"), PorterDuff.Mode.SRC_IN)

        //取两层绘制交集中的目标图像
        var cFilter6 = PorterDuffColorFilter(Color.parseColor("#5567c8ff"), PorterDuff.Mode.DST_IN)

        //只在源图像和目标图像不相交的地方绘制源图像
        var cFilter7 = PorterDuffColorFilter(Color.parseColor("#5567c8ff"), PorterDuff.Mode.SRC_OUT)

        //只在源图像和目标图像不相交的地方绘制目标图像
        var cFilter8 = PorterDuffColorFilter(Color.parseColor("#5567c8ff"), PorterDuff.Mode.DST_OUT)

        //在源图像和目标图像相交的地方绘制源图像，在不相交的地方绘制目标图像
        var cFilter9 = PorterDuffColorFilter(Color.parseColor("#5567c8ff"), PorterDuff.Mode.SRC_ATOP)

        //在源图像和目标图像相交的地方绘制目标图像，在不相交的地方绘制源图像
        var cFilter10 = PorterDuffColorFilter(Color.parseColor("#5567c8ff"), PorterDuff.Mode.DST_ATOP)

        //异或：去掉两图层交集部分
        var cFilter11 = PorterDuffColorFilter(Color.parseColor("#5567c8ff"), PorterDuff.Mode.XOR)

        //取两图层全部区域，交集部分颜色加深
        var cFilter12 = PorterDuffColorFilter(Color.parseColor("#5567c8ff"), PorterDuff.Mode.DARKEN)

        //取两图层全部区域，点亮交集部分颜色
        var cFilter13 = PorterDuffColorFilter(Color.parseColor("#5567c8ff"), PorterDuff.Mode.LIGHTEN)

        //取两图层交集部分叠加后颜色
        var cFilter14 = PorterDuffColorFilter(Color.parseColor("#5567c8ff"), PorterDuff.Mode.MULTIPLY)

        //滤色
        var cFilter15 = PorterDuffColorFilter(Color.parseColor("#5567c8ff"), PorterDuff.Mode.SCREEN)

        var cFilter16 = PorterDuffColorFilter(Color.parseColor("#5567c8ff"), PorterDuff.Mode.ADD)
        var cFilter17 = PorterDuffColorFilter(Color.parseColor("#5567c8ff"), PorterDuff.Mode.OVERLAY)

        lightColorFilters.add(cFilter0)
        lightColorFilters.add(cFilter1)
        lightColorFilters.add(cFilter2)
        lightColorFilters.add(cFilter3)
        lightColorFilters.add(cFilter4)
        lightColorFilters.add(cFilter5)
        lightColorFilters.add(cFilter6)
        lightColorFilters.add(cFilter7)

        lightColorFilters.add(cFilter8)
        lightColorFilters.add(cFilter9)
        lightColorFilters.add(cFilter10)
        lightColorFilters.add(cFilter11)
        lightColorFilters.add(cFilter12)
        lightColorFilters.add(cFilter13)
        lightColorFilters.add(cFilter14)
        lightColorFilters.add(cFilter15)

        lightColorFilters.add(cFilter16)
        lightColorFilters.add(cFilter17)

        return lightColorFilters
    }

    fun makeSrc() : Bitmap{
        val radius = bitmapWidth.div(3f)
        val bitmap = Bitmap.createBitmap(bitmapWidth.toInt(),bitmapWidth.toInt(),Bitmap.Config.ARGB_8888)
        val c = Canvas(bitmap)
        val p = Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.colorAccent)
        }
        c.drawRect(radius,radius,bitmapWidth.times(0.75f),bitmapWidth.times(0.75f),p)
        return bitmap
    }

    fun makeDst() : Bitmap{
        val radius = bitmapWidth.div(3f)
        val bitmap = Bitmap.createBitmap(radius.times(2).toInt()
            ,radius.times(2).toInt(),Bitmap.Config.ARGB_8888)
        val c = Canvas(bitmap)
        val p = Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.colorPrimary)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            c.drawOval(0f,0f,radius.times(2),radius.times(2),p)
        }
        return bitmap
    }

}