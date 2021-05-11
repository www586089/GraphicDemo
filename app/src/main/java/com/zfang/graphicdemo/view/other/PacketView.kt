package com.zfang.graphicdemo.view.other

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.activity.surfaceview.packet.Packet
import com.zfang.graphicdemo.common.px2Dp
import com.zfang.graphicdemo.utils.getScreenHeight
import com.zfang.graphicdemo.utils.getScreenWidth
import kotlin.random.Random

class PacketView(val ctx: Context, attrs: AttributeSet): View(ctx, attrs) {

    private val packetCount = 10
    private val DELAY = 5L
    private val FPS60 = (1000 / 60).toLong()


    private lateinit var packetArray: Array<Packet>
    private var flag = false
    private var canvasWidth = 0f
    private var canvasHeight = 0f

    private var bitmap: Bitmap
    private var bitmapWidth = 0f
    private var bitmapHeight = 0f

    private val paint = Paint()
    private var computeMatrix = Matrix()
    private val startRegion = RectF()
    private val endRegion = RectF()
    private var lastDrawTime = 0L


    init {
        bitmap = BitmapFactory.decodeResource(ctx.resources, R.drawable.red_packet_01)
        bitmapWidth = bitmap.width.toFloat()
        bitmapHeight = bitmap.height.toFloat()

        paint.style = Paint.Style.STROKE
        paint.color = Color.BLUE
        paint.strokeWidth = 1f
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (0f == canvasWidth) {
            canvasWidth = measuredWidth.toFloat()
            canvasHeight = measuredHeight.toFloat()

            if (canvasWidth > 0f && !flag) {
                initRegion()
                flag = true
                val random = Random(System.currentTimeMillis())
                val angle = 65f
                val PI = (Math.PI * (90 - angle) / 180.0)
                packetArray = Array(packetCount, init = {it ->
                    val startY = 0f
                    val endY = canvasHeight
                    val startX = canvasWidth / 3 + random.nextFloat() * (canvasWidth * 4 / 3)
                    val endX = (startX - canvasHeight / Math.tan(PI)).toFloat()

                    Packet(startX, endX, startY, endY, angle, bitmap, paint, random = random, computeMatrix, startRegion, endRegion).apply {
                        appearDuration = 300 //ms
                        middleDuration = 800
                        disappearDuration = 300
                        config(FPS60)
                    }
                })
            }
        }
    }

    private fun initRegion() {
        val width = getScreenWidth(ctx).toFloat()
        val height = getScreenHeight(ctx).toFloat()
        val offset = 5.px2Dp(ctx)
        startRegion.set(0f + offset, 0f + offset, width - offset, 100.px2Dp(ctx).toFloat() - offset)

        val bottomViewHeight = 100.px2Dp(ctx)
        val bottomDisappearHeight = 80.px2Dp(ctx)
        val viewHeight = canvasHeight
        endRegion.set(0f + offset, viewHeight - (bottomViewHeight + bottomDisappearHeight), width - offset, viewHeight - bottomViewHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (0L == lastDrawTime) {
            lastDrawTime = System.currentTimeMillis()
        }
        Log.d("zfang", "onDraw, step = ${System.currentTimeMillis() - lastDrawTime}")
        lastDrawTime = System.currentTimeMillis()
        if (null == canvas) {
            return
        }
        for (packet in packetArray) {
            packet.draw(canvas, canvasWidth, canvasHeight)
        }
//        postInvalidateDelayed(DELAY)
    }
}