package com.zfang.graphicdemo.view.surface

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.common.px2Dp
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RedPacketView(val ctx: Context, attributeSet: AttributeSet): SurfaceView(ctx, attributeSet) {

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
    init {
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

        mSurfaceHolder.addCallback(SurfaceHolderCB())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        canvasWidth = measuredWidth
        canvasHeight = measuredHeight
        Log.e(TAG, "canvasWidth = $canvasWidth, canvasHeight = $canvasHeight")
    }

    private fun initBitmap(): Unit {
        packetBitmap = BitmapFactory.decodeResource(ctx.resources, R.drawable.red_packet)
        packetBitmap?.let {
            Log.d(TAG, "bitmap, width = ${it.width}, height = ${it.height}")
            packetBitmapWidth = it.width
            packetBitmapHeight = it.height
            matrix.setScale(0.2f, 0.2f)
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

    private fun startRain() {
        //            Thread(WorkTask()).start()
        GlobalScope.launch { doRain() }
    }

    private fun doRain() {
        while (drawing) {
            try {
                mCanvas = mSurfaceHolder.lockCanvas()
                mCanvas?.run {
                    drawColor(Color.WHITE)
                    drawTest(this)
                    drawRain(this)
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
    }


    private fun drawSimplePacket(canvas: Canvas) {
        packetBitmap?.run {
            packetMatrix.reset()
            packetMatrix.setScale(0.5f, 0.5f)
            packetMatrix.postTranslate((canvasWidth / 2 - packetBitmapWidth / 4).toFloat(), (canvasHeight / 2 - packetBitmapHeight / 4).toFloat())
            packetMatrix.postRotate(45f, (canvasWidth / 2).toFloat(), (canvasHeight / 2).toFloat())
            canvas.drawBitmap(this, packetMatrix, paint)
//            canvas.drawBitmap(this, (canvasWidth / 2).toFloat(), 0.toFloat(), paint)
            canvas.drawBitmap(this, (canvasWidth / 2).toFloat(), (canvasHeight - packetBitmapHeight).toFloat(), paint)
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
}