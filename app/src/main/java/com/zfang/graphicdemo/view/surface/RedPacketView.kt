package com.zfang.graphicdemo.view.surface

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.*
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.activity.surfaceview.packet.PacketManager
import com.zfang.graphicdemo.activity.surfaceview.packet.TYPE_PACKET
import com.zfang.graphicdemo.common.px2Dp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RedPacketView(val ctx: Context, attributeSet: AttributeSet): SurfaceView(ctx, attributeSet), View.OnTouchListener {

    private val TAG = "RedPacketView"
    private var mSurfaceHolder: SurfaceHolder = holder
    private var mCanvas: Canvas? = null
    private var drawing = false
    private var haveStarted = false
    private var holderAvailable = false
    private var paint = Paint()
    private var testPaint = Paint()
    private var packetBitmap: Bitmap? = null
    private var packetMatrix = Matrix()
    private var matrixClick = Matrix()
    private var canvasWidth = 0
    private var canvasHeight = 0
    private var packetBitmapWidth = 0
    private var packetBitmapHeight = 0
    private var clickRectF = RectF()
    private var gestureDetector: GestureDetector
    private var path = Path()
    private val gestureListener = object : GestureDetector.OnGestureListener {
        override fun onDown(event: MotionEvent?): Boolean {
            Log.d(TAG, "onDown")

            val result = judgeClick(event)
            Log.d(TAG, "result = $result")
            return result
        }

        override fun onShowPress(event: MotionEvent?) {
            Log.d(TAG, "onShowPress")
        }

        override fun onSingleTapUp(event: MotionEvent?): Boolean {
            Log.d(TAG, "onSingleTapUp")
            return false
        }

        override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            Log.d(TAG, "onScroll")
            return false
        }

        override fun onLongPress(p0: MotionEvent?) {
            Log.d(TAG, "onLongPress")
        }

        override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            Log.d(TAG, "onFling")
            return false
        }

    }
    init {
        gestureDetector = GestureDetector(ctx, gestureListener)
        setOnTouchListener(this)
        initView()
        initBitmap()
    }

    private fun judgeClick(event: MotionEvent?): Boolean {
        if (null == event) {
            return false
        }

        val runningList = PacketManager.runningRainList
        val scale = 1.0f
        var left = 0f
        var top = 0f
        var right = 0f
        var bottom = 0f

        Log.e(TAG, "x = ${event.x}, y = ${event.y}")
        for (packetItem in runningList) {
            if (TYPE_PACKET != packetItem.rainType) {
                continue
            }
            matrixClick.reset()
            matrixClick.setScale(scale, scale)
            matrixClick.postTranslate(packetItem.currentX - scale * packetItem.bitmapWidth / 2, packetItem.currentY - scale * packetItem.bitmapHeight / 2)
            matrixClick.postRotate(90 - packetItem.angle, packetItem.currentX, packetItem.currentY)

            left = packetItem.currentX - scale * packetItem.bitmapWidth / 2
            top = packetItem.currentY - scale * packetItem.bitmapHeight / 2
            right = packetItem.currentX + scale * packetItem.bitmapWidth / 2
            bottom = packetItem.currentY + scale * packetItem.bitmapHeight / 2
            clickRectF.set(left, top, right, bottom)

//            matrixClick.mapRect(clickRectF)
            if (clickRectF.contains(event.x, event.y)) {
                return true
            }
        }

        return false
    }

    private fun initView() {
        paint.color = Color.RED
        paint.strokeWidth = 2.px2Dp(ctx).toFloat()
        paint.isAntiAlias = true

        testPaint.color = Color.BLUE
        testPaint.strokeWidth = 2.px2Dp(ctx).toFloat()
        testPaint.isAntiAlias = true
        testPaint.style = Paint.Style.STROKE

        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT)
        setZOrderOnTop(true)
        mSurfaceHolder.addCallback(SurfaceHolderCB())
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        canvasWidth = measuredWidth
        canvasHeight = measuredHeight
        Log.e(TAG, "canvasWidth = $canvasWidth, canvasHeight = $canvasHeight")
    }


    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private fun initBitmap() {
        packetBitmap = BitmapFactory.decodeResource(ctx.resources, R.drawable.red_packet_01)
        packetBitmap?.let {
            Log.d(TAG, "bitmap, width = ${it.width}, height = ${it.height}")
            packetBitmapWidth = it.width
            packetBitmapHeight = it.height
            val scale = 1f
            matrix.setScale(scale, scale)
        }
    }


    fun startDrawing() {
        drawing = true
        if (!haveStarted) {
            startRain()
        }
    }

    fun stopDrawing() {
        drawing = false
    }
    fun startRain() {
        //Thread(WorkTask()).start()
        GlobalScope.launch { doRain() }
    }

    private fun doRain() {
        if (drawing && holderAvailable) {
            haveStarted = true
        }
        while (drawing && holderAvailable) {
            try {
                mCanvas = mSurfaceHolder.lockCanvas()
                mCanvas?.run {
                    drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                    drawTest(this)
                    drawSimplePacket(this)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                mCanvas?.let { mSurfaceHolder.unlockCanvasAndPost(it) }
            }
        }
        haveStarted = false
    }

    private fun drawTest(canvas: Canvas) {
//        canvas.drawCircle((canvasWidth / 2).toFloat(), (canvasHeight / 2).toFloat(), 20.px2Dp(ctx).toFloat(), testPaint)
        canvas.drawLine(0f, (canvasHeight / 2).toFloat(), canvasWidth.toFloat(), (canvasHeight / 2).toFloat(), testPaint)
        canvas.drawLine((canvasWidth / 2).toFloat(), 0f, (canvasWidth / 2).toFloat(), canvasHeight.toFloat(), testPaint)
        PacketManager.appearRegion?.let { canvas.drawRect(it, testPaint) }
        PacketManager.endRegion?.let { canvas.drawRect(it, testPaint) }
        val width = 20.px2Dp(ctx)
        val height = 40.px2Dp(ctx)
        val centX = (canvasWidth / 2).toFloat()
        val centY = (canvasHeight / 2).toFloat()
        clickRectF.set(centX - width / 2, centY - height / 2, centX + width / 2, centY + height / 2)
//        canvas.drawRect(clickRectF, testPaint)

        canvas.save()
        canvas.rotate(45f)
        canvas.drawRect(clickRectF, testPaint)
        canvas.restore()
    }


    private fun drawSimplePacket(canvas: Canvas) {
        val packets = PacketManager.runningRainList
        val scale = 1f
        for (packetItem in packets) {
            if (packetItem.currentX <= -packetItem.outOffsetX) {
                continue
            }
            packetMatrix.reset()
            packetMatrix.setScale(scale, scale)
//            Log.e(TAG, "zfang, currentX = ${packetItem.currentX}, currentY = ${packetItem.currentY}")
            packetMatrix.postTranslate(packetItem.currentX - scale * packetItem.bitmapWidth / 2, packetItem.currentY - scale * packetItem.bitmapHeight / 2)
            packetMatrix.postRotate(90 - packetItem.angle, packetItem.currentX, packetItem.currentY)
            paint.alpha = (packetItem.currentAlpha * 0xff).toInt()
            canvas.drawBitmap(packetItem.bitmap, packetMatrix, paint)
//                canvas.drawCircle(packetItem.currentX, packetItem.currentY, 20f, paint)
        }
    }



    private fun drawRain(canvas: Canvas) {
        canvas.drawCircle(0F, 0F, (width / 2).toFloat(), paint)
    }

    /**
     * 线程实现
     */
    inner class WorkTask: Runnable {
        override fun run() {
            doRain()
        }
    }

    inner class SurfaceHolderCB: SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder?) {
            holderAvailable = true

            Log.d(TAG, "surfaceCreated")
            startRain()
        }

        override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
        ) {
            Log.d(TAG, "surfaceChanged")
        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
            holderAvailable = false
            Log.d(TAG, "surfaceDestroyed")
        }
    }
}