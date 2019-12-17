package com.zfang.graphicdemo.view.test

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.common.px2Dp

class TestView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private var TAG = "PorterDuffColorFilterView2"
    private var paintList: ArrayList<Paint> = ArrayList()
    private var dstBitmap: Bitmap = BitmapFactory.decodeResource(context!!.resources, R.drawable.composite_dst)
    private var srcBitmap: Bitmap = BitmapFactory.decodeResource(context!!.resources, R.drawable.composite_src)
    private var canvasWidth: Float = 0f
    private var canvasHeight: Float = 0f
    private var bitmapWidth = 0f
    private var bitmapHeight = 0f
    private var drawCount = 4
    private var bitmapPadding = 0f
    private var rect = Rect()
    private var textPaint = TextPaint()
    private var rectPaint = Paint()
    private var bitmapPaint = Paint()
    private var colorFilterMode: ArrayList<PorterDuffXfermode>

    init {

        val scaleMatrix = Matrix()
        scaleMatrix.postScale(0.6f, 0.6f, bitmapWidth / 2, bitmapHeight / 2)
        val oldWidth = dstBitmap.width
        val oldHeight = dstBitmap.height
        dstBitmap = Bitmap.createBitmap(dstBitmap, 0, 0, oldWidth, oldHeight, scaleMatrix, true)
        srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, oldWidth, oldHeight, scaleMatrix, true)
        bitmapWidth = dstBitmap.width.toFloat()
        bitmapHeight = dstBitmap.height.toFloat()


        textPaint.textSize = 14.px2Dp(getContext()).toFloat()
        textPaint.color = Color.RED

        val dashPathEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        rectPaint.setPathEffect(dashPathEffect)
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = 1.px2Dp(context!!).toFloat()
        colorFilterMode = getColorFilter()
    }
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        Log.e(TAG, "width = ${dstBitmap.width}, height = ${dstBitmap.height}")

        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val saveCount = canvas!!.saveLayer(0f, 0f, bitmapWidth, bitmapHeight, bitmapPaint)
        canvas.drawBitmap(dstBitmap, 0f, 0f, bitmapPaint)
        val mode = PorterDuff.Mode.SRC_ATOP
        bitmapPaint.xfermode = PorterDuffXfermode(mode)
        canvas.drawBitmap(srcBitmap, 0f, 0f, bitmapPaint)
        bitmapPaint.xfermode = null
        canvas.restoreToCount(saveCount)
        canvas.drawColor(Color.parseColor("#3367c8ff"))
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
        for (index in 0..17) {
            val paint = Paint()
            paint.isAntiAlias = true

            paintList.add(paint)
        }
    }

    fun getColorFilter(): ArrayList<PorterDuffXfermode> {
        var lightColorFilters = ArrayList<PorterDuffXfermode>(8)

        //所绘制源图像不会提交到画布上
        var cFilter0 = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        //只显示源图像
        var cFilter1 = PorterDuffXfermode(PorterDuff.Mode.SRC)

        //只显示目标图像
        var cFilter2 = PorterDuffXfermode(PorterDuff.Mode.DST)

        //正常绘制显示，源图像剧上显示
        var cFilter3 = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)

        //正常绘制显示，目标图像居上显示（上下层都显示）
        var cFilter4 = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)

        //取两层绘制交集中的源图像
        var cFilter5 = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        //取两层绘制交集中的目标图像
        var cFilter6 = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

        //只在源图像和目标图像不相交的地方绘制源图像
        var cFilter7 = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)

        //只在源图像和目标图像不相交的地方绘制目标图像
        var cFilter8 = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)

        //在源图像和目标图像相交的地方绘制源图像，在不相交的地方绘制目标图像
        var cFilter9 = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)

        //在源图像和目标图像相交的地方绘制目标图像，在不相交的地方绘制源图像
        var cFilter10 = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)

        //异或：去掉两图层交集部分
        var cFilter11 = PorterDuffXfermode(PorterDuff.Mode.XOR)

        //取两图层全部区域，交集部分颜色加深
        var cFilter12 = PorterDuffXfermode(PorterDuff.Mode.DARKEN)

        //取两图层全部区域，点亮交集部分颜色
        var cFilter13 = PorterDuffXfermode(PorterDuff.Mode.LIGHTEN)

        //取两图层交集部分叠加后颜色
        var cFilter14 = PorterDuffXfermode(PorterDuff.Mode.MULTIPLY)

        //滤色
        var cFilter15 = PorterDuffXfermode(PorterDuff.Mode.SCREEN)

        var cFilter16 = PorterDuffXfermode(PorterDuff.Mode.ADD)
        var cFilter17 = PorterDuffXfermode(PorterDuff.Mode.OVERLAY)

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

}