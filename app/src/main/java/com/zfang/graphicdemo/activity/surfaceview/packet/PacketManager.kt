package com.zfang.graphicdemo.activity.surfaceview.packet

import android.animation.ValueAnimator
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.LinearInterpolator
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.random.Random

object PacketManager {

    private val TAG = "PacketManager"
    val packetList: CopyOnWriteArrayList<PacketItem> = CopyOnWriteArrayList()
    get
    var startRegion: RectF? = null
    var endRegion: RectF? = null
    private lateinit var packetItemAnimator: ValueAnimator
    private val handler: Handler = Handler(Looper.getMainLooper())

    /**
     * 生成红包数据
     */
    fun generatePacket(rainId: String, startRegion: RectF, endRegion: RectF, packetCount: Int = 40) {
        packetList.clear()
        this.startRegion = startRegion
        this.endRegion = endRegion
        for (index in 0 until packetCount) {
            val x = getRandomValue(startRegion.left, startRegion.right)
            val y = getRandomValue(startRegion.top, startRegion.bottom)
            val endX = getRandomValue(endRegion.left, endRegion.right)
            val endY = getRandomValue(endRegion.top, endRegion.bottom)
            val degree = getRandomValue(-45f, 45f)
            Log.d(TAG, "x = $x, y = $y, degree = $degree")
            packetList.add(PacketItem(rainId, 2000, x, y, endX, endY, degree).apply {
                currentX = pX
                currentY = pY
            })
        }
    }


    fun startRainAnimation() {
        packetItemAnimator = ValueAnimator.ofFloat(0f, 1f)
        packetItemAnimator.duration = 1000
        packetItemAnimator.repeatMode = ValueAnimator.RESTART
        packetItemAnimator.repeatCount = ValueAnimator.INFINITE
        packetItemAnimator.interpolator = LinearInterpolator()
        packetItemAnimator.addUpdateListener { animation ->
            calculate()
        }
        packetItemAnimator.start()
        handler.postDelayed({
            if (packetItemAnimator.isRunning) {
                packetItemAnimator.end()
            }
        }, 8000)
    }

    private fun getRandomValue(start: Float, end: Float): Float {
        val random = Random(System.currentTimeMillis())
        return random.nextDouble(start.toDouble(), end.toDouble()).toFloat()
    }

    private fun calculate() {
        for (item in packetList) {
            if (0L == item.startTime) {
                item.startTime = System.currentTimeMillis()
            }

            var fraction = ((System.currentTimeMillis().toDouble() - item.startTime) / item.duration.toDouble()).toFloat()
            if (fraction > 1) {
                fraction = 1f
            }

            item.currentX = item.pX + fraction * (item.endX - item.pX)
            item.currentY = item.pY + fraction * (item.endY - item.pY)
        }
    }

    interface RainAnimationEnd {
        fun onEnd()
    }
}