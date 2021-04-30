package com.zfang.graphicdemo.view.surface

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.*
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.activity.surfaceview.packet.PacketItem
import com.zfang.graphicdemo.activity.surfaceview.packet.PacketManager
import com.zfang.graphicdemo.common.px2Dp
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RedPacketView(val ctx: Context, attributeSet: AttributeSet): SurfaceView(ctx, attributeSet), View.OnTouchListener {

    private val TAG = "RedPacketView"
    private var mSurfaceHolder: SurfaceHolder = holder
    private var mCanvas: Canvas? = null
    private var drawing = false
    private var paint = Paint()
    private var testPaint = Paint()
    private var packetBitmap: Bitmap? = null
    private var packetMatrix = Matrix()
    private var canvasWidth = 0
    private var canvasHeight = 0
    private var packetBitmapWidth = 0
    private var packetBitmapHeight = 0
    private var gestureDetector: GestureDetector
    private val gestureListener = object : GestureDetector.OnGestureListener {
        override fun onDown(event: MotionEvent?): Boolean {
            Log.d(TAG, "onDown")

            return true
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

    private fun initView() {
        paint.color = Color.RED
        paint.strokeWidth = 2.px2Dp(ctx).toFloat()
        paint.isAntiAlias = true

        testPaint.color = Color.BLUE
        testPaint.strokeWidth = 2.px2Dp(ctx).toFloat()
        testPaint.isAntiAlias = true

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
        packetBitmap = BitmapFactory.decodeResource(ctx.resources, R.drawable.red_packet)
        packetBitmap?.let {
            Log.d(TAG, "bitmap, width = ${it.width}, height = ${it.height}")
            packetBitmapWidth = it.width
            packetBitmapHeight = it.height
            matrix.setScale(0.2f, 0.2f)
        }
    }


    private fun startRain() {
        //            Thread(WorkTask()).start()
        GlobalScope.launch { doRain() }
    }

    private fun doRain() {
        while (drawing) {
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
    }

    private fun drawTest(canvas: Canvas) {
        canvas.drawCircle((canvasWidth / 2).toFloat(), (canvasHeight / 2).toFloat(), 20.px2Dp(ctx).toFloat(), testPaint)
        canvas.drawLine(0f, (canvasHeight / 2).toFloat(), canvasWidth.toFloat(), (canvasHeight / 2).toFloat(), testPaint)
        canvas.drawLine((canvasWidth / 2).toFloat(), 0f, (canvasWidth / 2).toFloat(), canvasHeight.toFloat(), testPaint)
//        PacketManager.startRegion?.let { canvas.drawRect(it, testPaint) }
//        PacketManager.endRegion?.let { canvas.drawRect(it, testPaint) }
    }


    private fun drawSimplePacket(canvas: Canvas) {
        packetBitmap?.run {

            val packets = PacketManager.packetList
            val scale = 0.2f
            for (packetItem in packets) {
                packetMatrix.reset()
                packetMatrix.setScale(scale, scale)
                packetMatrix.postTranslate(packetItem.currentX - scale * packetBitmapWidth / 2, packetItem.currentY - scale * packetBitmapHeight / 2)
                packetMatrix.postRotate(packetItem.degree, packetItem.currentX, packetItem.currentY)
                canvas.drawBitmap(this, packetMatrix, paint)
                canvas.drawCircle(packetItem.currentX, packetItem.currentY, 20f, paint)
            }
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
            drawing = true
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
            drawing = false
            Log.d(TAG, "surfaceDestroyed")
        }
    }
}