package com.zfang.graphicdemo.view.shader

import android.content.Context
import android.graphics.*
import android.util.ArrayMap
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.common.px2Dp

class ShowComposeShaderView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var canvasHeight = 0f
    private var canvasWidth = 0f
    private var rectPaint = Paint()
    private var dashPaint = Paint()
    private var textBounds = Rect()
    private var shader: Shader? = null
    private var pathShader:Shader? = null

    private var circlePaint = Paint()
    private var shaderCircle: Shader? = null
    private var pathPaint = Paint()
    private var textPaint = Paint()


    val paddingH = 6.px2Dp(context!!).toFloat()
    val paddingV = 6.px2Dp(context!!).toFloat()
    var rectWidth = 0f
    var rectHeight = 0f
    val rLeft = paddingH
    val rTop = paddingV
    val lineCount = 4
    var radius = 0f

    var bitmap = BitmapFactory.decodeResource(context!!.resources, R.drawable.filter_icon_film2)
    var bitmapCircle = BitmapFactory.decodeResource(context!!.resources, R.drawable.filter_icon_fresh2)
    val map = SparseArray<Shader>()

    init {
        rectPaint.style = Paint.Style.STROKE
        rectPaint.color = Color.RED
        rectPaint.strokeWidth  = 1.px2Dp(context!!).toFloat()
        rectPaint.pathEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)

        circlePaint.style = Paint.Style.FILL

        dashPaint.pathEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        dashPaint.strokeWidth = 1.px2Dp(context).toFloat()
        dashPaint.style = Paint.Style.STROKE
        dashPaint.color = Color.BLACK

        pathPaint.style = Paint.Style.FILL_AND_STROKE
        pathPaint.strokeWidth = 2.px2Dp(context).toFloat()
        pathPaint.color = Color.RED
        pathPaint.isAntiAlias = true
        pathPaint.strokeJoin = Paint.Join.ROUND

        textPaint.textSize = 14.px2Dp(context).toFloat()
        textPaint.color = Color.GREEN
        textPaint.style = Paint.Style.STROKE


        var oldWidth = bitmap.width.toFloat()
        var oldHeight = bitmap.height.toFloat()
        var scaleMatrix = Matrix()
        scaleMatrix.postScale(0.6f, 0.6f, oldWidth / 2, oldHeight / 2)

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, oldWidth.toInt(), oldHeight.toInt(), scaleMatrix, true)


        oldWidth = bitmapCircle.width.toFloat()
        oldHeight = bitmapCircle.height.toFloat()
        scaleMatrix = Matrix()
        scaleMatrix.postScale(0.6f, 0.6f, oldWidth / 2, oldHeight / 2)

        bitmapCircle = Bitmap.createBitmap(bitmapCircle, 0, 0, oldWidth.toInt(), oldHeight.toInt(), scaleMatrix, true)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var pLeft: Float
        var pTop: Float

        for (mode in PorterDuff.Mode.values()) {
            val index = mode.ordinal
            val name = mode.name

            pLeft = (index % lineCount + 1) * paddingH + (index % lineCount) * 2 * radius
            pTop = (index / lineCount + 1) * paddingV + (index / lineCount) * 2 * radius
            val centerX = pLeft + radius
            val centerY = pTop + radius

            var shader = map.get(index)
            if (null == shader) {
                val dstShader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
                val srcShader = RadialGradient(centerX, centerY, radius, Color.RED, Color.BLUE, Shader.TileMode.CLAMP)
                shader = ComposeShader(dstShader, srcShader, mode)
                map.put(index, shader)
            }

            circlePaint.shader = shader

            canvas!!.drawCircle(centerX, centerY, radius, circlePaint)
            textPaint.getTextBounds(name, 0, name.length, textBounds)
            canvas.drawRect(centerX - radius, centerY - radius, centerX + radius, centerY + radius, rectPaint)
            canvas.drawText(name, centerX - textBounds.width() / 2, centerY + textBounds.height() / 2, textPaint)
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        canvasWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        canvasHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()

        rectWidth = canvasWidth / 2
        rectHeight = rectWidth / 3

        radius = 0.5f * (canvasWidth - (lineCount + 1) * paddingH) / lineCount
    }
}