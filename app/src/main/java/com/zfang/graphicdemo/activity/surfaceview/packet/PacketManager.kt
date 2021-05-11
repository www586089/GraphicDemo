package com.zfang.graphicdemo.activity.surfaceview.packet

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import com.zfang.graphicdemo.R
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import rx.schedulers.Schedulers
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.random.Random

object PacketManager {

    private val TAG = "PacketManager"
    val packetList: CopyOnWriteArrayList<PacketItem> = CopyOnWriteArrayList()
    get
    val runningRainList: CopyOnWriteArrayList<PacketItem> = CopyOnWriteArrayList()
    val liuXingList: CopyOnWriteArrayList<PacketItem> = CopyOnWriteArrayList()
    val starList: CopyOnWriteArrayList<PacketItem> = CopyOnWriteArrayList()
    var appearRegion: RectF? = null
    var endRegion: RectF? = null
    private lateinit var packetItemAnimator: ValueAnimator
    private var aggresstionAnimation: ValueAnimator? = null
    private var handlerThread: HandlerThread? = null
    private var computeThread: HandlerThread? = null
    private var handler: Handler? = null
    private var computeHandler: Handler? = null
    private var packetBitmap: Bitmap? = null
    private var liuxingBitmap: Bitmap? = null
    private var starBitmap: Bitmap? = null

    private var packetBitmapWidth = 0
    private var packetBitmapHeight = 0
    private var packetOutOffsetX = 0f

    private var liuxingBitmapWidth = 0
    private var liuxingBitmapHeight = 0

    private var starBitmapWidth = 0
    private var starBitmapHeight = 0
    private var dispacter: ExecutorCoroutineDispatcher? = null


    fun init(ctx: Context, angle: Float) {
        dispacter = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        packetBitmap = BitmapFactory.decodeResource(ctx.resources, R.drawable.red_packet_01)
        packetBitmap?.let {
            Log.d(TAG, "bitmap, width = ${it.width}, height = ${it.height}")
            packetBitmapWidth = it.width
            packetBitmapHeight = it.height
            packetOutOffsetX = (Math.sqrt(Math.pow(it.width.toDouble(), 2.toDouble()) + Math.pow(it.height.toDouble(), 2.toDouble())) / 2).toFloat()
        }

        liuxingBitmap = BitmapFactory.decodeResource(ctx.resources, R.drawable.icon_liuxing)
        liuxingBitmap?.let {
            Log.d(TAG, "liuxingBitmap, width = ${it.width}, height = ${it.height}")
            liuxingBitmapWidth = it.width
            liuxingBitmapHeight = it.height
        }

        starBitmap = BitmapFactory.decodeResource(ctx.resources, R.drawable.icon_star)
        starBitmap?.let {
            Log.d(TAG, "liuxingBitmap, width = ${it.width}, height = ${it.height}")
            starBitmapWidth = it.width
            starBitmapHeight = it.height
        }
    }
    /**
     * 生成相关动画数据
     */
    fun generateAnimationItem(ctx: Context, rainId: String, appearRegion: RectF, endRegion: RectF, packetCount: Int = 40, angle: Float) {
        if (null == packetBitmap) {
            init(ctx, angle)
        }
//        handlerThread.start()
        packetList.clear()
        liuXingList.clear()
        starList.clear()
        runningRainList.clear()
        this.appearRegion = appearRegion
        this.endRegion = endRegion
        generatePacket(rainId, appearRegion, endRegion, packetCount, angle)     //红包
        generateRainPoint(rainId, appearRegion, endRegion, packetCount / 3, angle)  //流星
        generateStar(rainId, appearRegion, endRegion, packetCount / 2, angle)       //星星
//        if (packetCount < 10) {
//            runningRainList.addAll(packetList)
//        } else {
            scheduleItemAnimation(packetCount, 3, 2)
//        }
    }

    private fun scheduleItemAnimation(packetCount: Int, liuXingRatio: Int, starRatio: Int) {
        val interval = ((8000 - 3500) / packetCount).toLong()
        Log.e(TAG, "interval = $interval")
        val start = System.currentTimeMillis()
        rx.Observable.interval(interval, TimeUnit.MILLISECONDS)
                .takeWhile { System.currentTimeMillis() - start < 4500}
                .subscribeOn(Schedulers.computation())
                .map { it.toInt() }
                .subscribe({ index ->
                    Log.e(TAG, "index = $index")
                    if (0 == index % liuXingRatio && !liuXingList.isEmpty()) {
                        runningRainList.add(liuXingList.removeFirst())
                    }
                    if (0 == index % starRatio && !starList.isEmpty()) {
                        runningRainList.add(starList.removeFirst())
                    }
                    if (!packetList.isEmpty()) {
                        runningRainList.add(packetList.removeFirst())
                    }
//                    handler?.post { startPropertyAnimation() }
                }, { throwable ->
                    Log.e(TAG, "throwable = ${throwable.message}")
                })
    }

    private fun startPropertyAnimation2() {
        for (item in runningRainList) {
            if (item.started) {
                continue
            }
            item.started = true
            val appearAnimator = ObjectAnimator.ofObject(PacketItemEvaluator(), item.apply {
                startX = pointsArray[0][0]
                endX = pointsArray[1][0]
                startY = pointsArray[0][1]
                endY = pointsArray[1][1]
                currentAlpha = 0f
                state = STATE_APPEAR
            }, item.apply {
                startX = pointsArray[0][0]
                endX = pointsArray[1][0]
                startY = pointsArray[0][1]
                endY = pointsArray[1][1]
                currentAlpha = 1f
            })
            appearAnimator.addUpdateListener {
                val animateItem = it.animatedValue as PacketItem
                animateItem.run {
                    item.currentAlpha = currentAlpha
                    item.currentX = currentX
                    item.currentY = currentY
                }
            }
            appearAnimator.addListener(onEnd = {
                item.state = STATE_MIDDLE
                val middleAnimator = ObjectAnimator.ofObject(PacketItemEvaluator(), item.apply {
                    startX = pointsArray[1][0]
                    endX = pointsArray[2][0]
                    startY = pointsArray[1][1]
                    endY = pointsArray[2][1]
                    state = STATE_APPEAR
                }, item.apply {
                    startX = pointsArray[1][0]
                    endX = pointsArray[2][0]
                    startY = pointsArray[1][1]
                    endY = pointsArray[2][1]
                })
                middleAnimator.addUpdateListener {
                    val animateItem = it.animatedValue as PacketItem
                    animateItem.run {
                        item.currentX = currentX
                        item.currentY = currentY
                    }
                }
                middleAnimator.addListener(onEnd = {
                    val disappearAnimator = ObjectAnimator.ofObject(PacketItemEvaluator(), item.apply {
                        startX = pointsArray[2][0]
                        endX = pointsArray[3][0]
                        startY = pointsArray[2][1]
                        endY = pointsArray[3][1]
                        state = STATE_DISAPPEAR
                        currentAlpha = 1f
                    }, item.apply {
                        startX = pointsArray[2][0]
                        endX = pointsArray[3][0]
                        startY = pointsArray[2][1]
                        endY = pointsArray[3][1]
                        currentAlpha = 0f
                    })
                    disappearAnimator.addUpdateListener {
                        val animateItem = it.animatedValue as PacketItem
                        animateItem.run {
                            item.currentX = currentX
                            item.currentY = currentY
                            item.currentAlpha = currentAlpha
                        }
                    }
                    disappearAnimator.interpolator = LinearInterpolator()
                    disappearAnimator.duration = item.alphaDisappearDuration
                    disappearAnimator.start()
                })
                middleAnimator.interpolator = LinearInterpolator()
                middleAnimator.duration = item.middleDuration
                middleAnimator.start()
            })
            appearAnimator.interpolator = LinearInterpolator()
            appearAnimator.duration = item.alphaAppearDuration
            appearAnimator.start()
        }
    }
    private fun startPropertyAnimation() {
        for (item in runningRainList) {
            if (item.started) {
                continue
            }
            item.started = true
            val animatorSet = AnimatorSet()
            val animatorX = ValueAnimator.ofFloat(item.pointsArray[0][0], item.pointsArray[1][0])
            animatorX.duration = item.alphaAppearDuration
            animatorX.interpolator = LinearInterpolator()
            animatorX.addUpdateListener {
                item.currentX = it.animatedValue as Float
                if (item.currentX <= item.outOffsetX) {
//                    animatorSet.end()
                }
            }
            animatorX.start()
            val animatorY = ValueAnimator.ofFloat(item.pointsArray[0][1], item.pointsArray[1][1])
            animatorY.duration = item.alphaAppearDuration
            animatorY.interpolator = LinearInterpolator()
            animatorY.addUpdateListener {
                item.currentY = it.animatedValue as Float
            }

            animatorY.start()

            val alphaAnimation = ValueAnimator.ofFloat(0f, 1f)
            alphaAnimation.duration = item.alphaAppearDuration
            alphaAnimation.interpolator = LinearInterpolator()
            alphaAnimation.addUpdateListener {
                item.currentAlpha = it.animatedValue as Float
            }
            alphaAnimation.start()

            animatorSet.addListener(onEnd = {
                val animatorMiddleSet = AnimatorSet()
                val animatorMiddleX = ValueAnimator.ofFloat(item.pointsArray[1][0], item.pointsArray[2][0])
                animatorMiddleX.duration = item.middleDuration
                animatorMiddleX.interpolator = LinearInterpolator()
                animatorMiddleX.addUpdateListener {
                    item.currentX = it.animatedValue as Float
                    if (item.currentX < item.outOffsetX) {
//                        animatorMiddleSet.end()
                    }
                }
                val animatorMiddleY = ValueAnimator.ofFloat(item.pointsArray[1][1], item.pointsArray[2][1])
                animatorMiddleY.duration = item.middleDuration
                animatorMiddleY.interpolator = LinearInterpolator()
                animatorMiddleY.addUpdateListener {
                    item.currentY = it.animatedValue as Float
                }

                animatorMiddleSet.addListener(onEnd = {
                    val animatorDisappearSet = AnimatorSet()
                    val animatorDisappearX = ValueAnimator.ofFloat(item.pointsArray[2][0], item.pointsArray[3][0])
                    animatorDisappearX.duration = item.alphaDisappearDuration
                    animatorDisappearX.interpolator = LinearInterpolator()
                    animatorDisappearX.addUpdateListener {
                        item.currentX = it.animatedValue as Float
                        if (item.currentX < item.outOffsetX) {
//                            animatorDisappearSet.end()
                        }
                    }
                    val animatorDisappearY = ValueAnimator.ofFloat(item.pointsArray[2][1], item.pointsArray[3][1])
                    animatorDisappearY.duration = item.alphaDisappearDuration
                    animatorDisappearY.interpolator = LinearInterpolator()
                    animatorDisappearY.addUpdateListener {
                        item.currentY = it.animatedValue as Float
                    }

                    val animatorDisappearAlphaAnimation = ValueAnimator.ofFloat(1f, 0f)
                    animatorDisappearAlphaAnimation.duration = item.alphaDisappearDuration
                    animatorDisappearAlphaAnimation.interpolator = LinearInterpolator()
                    animatorDisappearAlphaAnimation.addUpdateListener {
                        item.currentAlpha = it.animatedValue as Float
                    }

                    animatorDisappearSet.playTogether(animatorDisappearX, animatorDisappearY, animatorDisappearAlphaAnimation)
                    animatorDisappearSet.start()
                })
                animatorMiddleSet.playTogether(animatorMiddleX, animatorMiddleY)
                animatorMiddleSet.start()
            })
            animatorSet.playTogether(animatorX, animatorY, alphaAnimation)
            animatorSet.start()
        }
    }

    /**
     * 生成流星数据
     */
    private fun generateRainPoint(rainId: String, appearRegion: RectF, endRegion: RectF, packetCount: Int = 40, angle: Float) {
        val PI = getPI(angle)
        val random = Random(System.currentTimeMillis())
        for (index in 0 until packetCount) {
            val appearStartX = getRandomValue(appearRegion.left, appearRegion.right, random)
            val appearStartY = appearRegion.top


            val appearEndX = (appearStartX - appearRegion.height() / Math.tan(PI)).toFloat()
            val appearEndY = appearRegion.bottom

            val middleEndX = (appearStartX - (endRegion.top - appearRegion.top) / Math.tan(PI)).toFloat()
            val middleEndY = endRegion.top

            val endX = appearStartX - (endRegion.bottom / Math.tan(PI)).toFloat()
            val endY = endRegion.bottom

            Log.d(TAG, "x = $appearStartX, y = $appearStartY, degree = $angle")
            liuXingList.add(PacketItem(rainId, 500 * scale(), appearStartX, appearStartY, endX, endY, angle, TYPE_RIAN).apply {
                pointsArray[0][0] = appearStartX
                pointsArray[0][1] = appearStartY

                pointsArray[1][0] = appearEndX
                pointsArray[1][1] = appearEndY

                pointsArray[2][0] = middleEndX
                pointsArray[2][1] = middleEndY

                pointsArray[3][0] = endX
                pointsArray[3][1] = endY

                currentX = this.startX
                currentY = this.startY
                alphaAppearDuration = 300L * scale()
                alphaDisappearDuration = 300L * scale()

                bitmap = liuxingBitmap!!
                bitmapWidth = liuxingBitmapWidth
                bitmapHeight = liuxingBitmapHeight
            })
        }
    }

    /**
     * 生成星星数据
     */
    private fun generateStar(rainId: String, appearRegion: RectF, endRegion: RectF, packetCount: Int = 40, angle: Float) {
        val PI = getPI(angle)
        val random = Random(System.currentTimeMillis())
        for (index in 0 until packetCount) {
            val appearStartX = getRandomValue(appearRegion.left, appearRegion.right, random)
            val appearStartY = appearRegion.top


            val appearEndX = (appearStartX - appearRegion.height() / Math.tan(PI)).toFloat()
            val appearEndY = appearRegion.bottom

            val middleEndX = (appearStartX - (endRegion.top - appearRegion.top) / Math.tan(PI)).toFloat()
            val middleEndY = endRegion.top

            val endX = appearStartX - (endRegion.bottom / Math.tan(PI)).toFloat()
            val endY = endRegion.bottom

            Log.d(TAG, "x = $appearStartX, y = $appearStartY, degree = $angle")
            starList.add(PacketItem(rainId, 500 * scale(), appearStartX, appearStartY, endX, endY, angle, TYPE_STAR).apply {
                pointsArray[0][0] = appearStartX
                pointsArray[0][1] = appearStartY

                pointsArray[1][0] = appearEndX
                pointsArray[1][1] = appearEndY

                pointsArray[2][0] = middleEndX
                pointsArray[2][1] = middleEndY

                pointsArray[3][0] = endX
                pointsArray[3][1] = endY

                currentX = this.startX
                currentY = this.startY
                alphaAppearDuration = 300L * scale()
                alphaDisappearDuration = 300L * scale()

                bitmap = starBitmap!!
                bitmapWidth = starBitmapWidth
                bitmapHeight = starBitmapHeight
            })
        }
    }
    /**
     * 生成红包数据
     */
    private fun generatePacket(rainId: String, appearRegion: RectF, endRegion: RectF, packetCount: Int = 40, angle: Float) {
        val PI = getPI(angle)
        val random = Random(System.currentTimeMillis())
        for (index in 0 until packetCount) {
            val appearStartX = getRandomValue(appearRegion.left, appearRegion.right, random)
            val appearStartY = appearRegion.top


            val appearEndX = (appearStartX - appearRegion.height() / Math.tan(PI)).toFloat()
            val appearEndY = appearRegion.bottom

            val middleEndX = (appearStartX - (endRegion.top - appearRegion.top) / Math.tan(PI)).toFloat()
            val middleEndY = endRegion.top

            val endX = appearStartX - (endRegion.bottom / Math.tan(PI)).toFloat()
            val endY = endRegion.bottom

            Log.d(TAG, "x = $appearStartX, y = $appearStartY, degree = $angle")
            packetList.add(PacketItem(rainId, 1600 * scale(), appearStartX, appearStartY, endX, endY, angle, TYPE_PACKET).apply {
                pointsArray[0][0] = appearStartX
                pointsArray[0][1] = appearStartY

                pointsArray[1][0] = appearEndX
                pointsArray[1][1] = appearEndY

                pointsArray[2][0] = middleEndX
                pointsArray[2][1] = middleEndY

                pointsArray[3][0] = endX
                pointsArray[3][1] = endY

                currentX = this.startX
                currentY = this.startY
                alphaAppearDuration = 300L * scale()
                alphaDisappearDuration = 300L * scale()
                outOffsetX = packetOutOffsetX

                bitmap = packetBitmap!!
                bitmapWidth = packetBitmapWidth
                bitmapHeight = packetBitmapHeight
            })
        }
    }

    fun getPI(angle: Float) = (angle / 180.0) * Math.PI

    fun startRainAnimation(rainAnimationEnd: RainAnimationEnd) {
//        packetItemAnimator = ValueAnimator.ofFloat(0f, 1f)
//        packetItemAnimator.duration = 1000
//        packetItemAnimator.repeatMode = ValueAnimator.RESTART
//        packetItemAnimator.repeatCount = ValueAnimator.INFINITE
//        packetItemAnimator.interpolator = LinearInterpolator()
//        packetItemAnimator.addUpdateListener { animation ->
//            calculate()
//        }
//        packetItemAnimator.start()
//        handler?.postDelayed({
//            if (packetItemAnimator.isRunning) {
//                packetItemAnimator.end()
//            }
//        }, 8000)
        start(rainAnimationEnd)
    }

    fun start(rainAnimationEnd: RainAnimationEnd) {
//        val appearAnimation = ValueAnimator.ofFloat(0f, 1f)
//        appearAnimation.interpolator = LinearInterpolator()
//        appearAnimation.duration = (0.6f * 1000f).toLong()
//        appearAnimation.addUpdateListener { animator ->
//            Log.d(TAG, "zfang, appear, fraction = ${animator.animatedFraction}")
//            calculateAlpha()
//            calculate2()
//        }
//
//        val middleAnimation = ValueAnimator.ofFloat(0f, 1f)
//        middleAnimation.interpolator = LinearInterpolator()
//        middleAnimation.duration = (2f * 1000).toLong()
//        middleAnimation.addUpdateListener { animator ->
//            Log.d(TAG, "zfang, middle, fraction = ${animator.animatedFraction}")
//            calculate2()
//        }
//
//        val disappearAnimation = ValueAnimator.ofFloat(0F, 1F)
//        disappearAnimation.interpolator = LinearInterpolator()
//        disappearAnimation.duration = (1.6f * 1000).toLong()
//        disappearAnimation.addUpdateListener { animator ->
//            Log.d(TAG, "zfang, disappear, fraction = ${animator.animatedFraction}")
//            calculateDisappearAlpha()
//            calculate2()
//        }
//
//        val animatorSet = AnimatorSet()
//        animatorSet.playSequentially(appearAnimation, middleAnimation, disappearAnimation)
//        animatorSet.start()

        Log.d(TAG, "thread = ${Thread.currentThread()}")
        if (null == handlerThread) {
            handlerThread = HandlerThread("rainAnimationThread")
            handlerThread?.start()
            handler = Handler(handlerThread!!.looper)
        }
        if (null == computeThread) {
            computeThread = HandlerThread("computeThread")
            computeThread?.start()
            computeHandler = Handler(computeThread!!.looper)
        }

        aggresstionAnimation?.let {
            if (it.isRunning) {
                endAnimation(rainAnimationEnd)
            }
        }

        handler?.post {
            aggresstionAnimation = ValueAnimator.ofFloat(0f, 1f)
            aggresstionAnimation?.apply {
                duration = 2000
                repeatMode = ValueAnimator.RESTART
                repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                addUpdateListener { animation ->
                    Log.e("zfang", "update, fraction = ${animation.animatedFraction}")
                    calculateAggressions()
                }
                start()
                handler!!.postDelayed({
                    endAnimation(rainAnimationEnd)
                }, 8000)
            }
        }
    }

    private fun endAnimation(rainAnimationEnd: RainAnimationEnd) {
        Log.e(TAG, "zfang end calculateAggressions start ---->")
        if (aggresstionAnimation?.isRunning == true) {
            aggresstionAnimation?.end()
            rainAnimationEnd.onEnd()
            Log.e(TAG, "zfang end calculateAggressions end <-------")
        }
    }

    private fun getRandomValue(start: Float, end: Float, random: Random): Float {
        return random.nextDouble(start.toDouble(), end.toDouble()).toFloat()
    }

    private fun scale(): Long {
//        return 1000_000
        return 1
    }
    private fun currentTime(): Long {
//        return SystemClock.elapsedRealtimeNanos()
        return System.currentTimeMillis()
    }
    private fun calculateAggressions() {
        val elementsToRemove = ArrayList<PacketItem>()
        for (item in runningRainList) {
            when (item.state) {
                STATE_INVALIDATE -> {
                    item.startTime = currentTime()
                    item.state = STATE_APPEAR
                    item.currentAlpha = 0f
                    item.startX = -1f
                }

                STATE_APPEAR -> {
                    if (currentTime() - item.startTime >= item.alphaAppearDuration) {
                        item.lastTime = currentTime()
                        Log.e(TAG, "zfang, time AppearDuration = ${currentTime() - item.startTime}")
                        item.state = STATE_MIDDLE
                        item.currentAlpha = 1.0f
                        item.startX = -1f
//                        Log.e(TAG, "appear, currentAlpha = ${item.currentAlpha}")
                        continue
                    }

                    if (item.currentX <= -item.outOffsetX) {
                        elementsToRemove.add(item)
                        continue
                    }
                    val fraction = ((currentTime() - item.startTime) / item.alphaAppearDuration.toDouble()).toFloat()
                    if (fraction - item.lastFraction < 0.01) {
                        item.lastFraction = fraction
                        break
                    }
                    item.apply {
                        if (-1f == startX) {
                            startX = pointsArray[0][0]
                            endX = pointsArray[1][0]
                            startY = pointsArray[0][1]
                            endY = pointsArray[1][1]
                        }


                        computeCurrentPoint(this, fraction, startX, endX, startY, endY)
                        currentAlpha = fraction
                    }
                }

                STATE_MIDDLE -> {
                    if (currentTime() - item.lastTime >= item.middleDuration) {
                        item.lastTime = currentTime()
                        Log.e(TAG, "zfang, time middleDuration = ${currentTime() - item.startTime - item.alphaAppearDuration}")
                        item.state = STATE_DISAPPEAR
                        item.startX = -1f
                        continue
                    }

                    if (item.currentX <= -item.outOffsetX) {
                        elementsToRemove.add(item)
                        continue
                    }

                    val fraction = ((currentTime() - item.lastTime) / item.middleDuration.toDouble()).toFloat()
                    if (fraction - item.lastFraction < 0.01) {
                        item.lastFraction = fraction
                        break
                    }

                    item.run {
                        if (-1f == startX) {
                            startX = pointsArray[1][0]
                            endX = pointsArray[2][0]
                            startY = pointsArray[1][1]
                            endY = pointsArray[2][1]
                        }
                        computeCurrentPoint(this, fraction, startX, endX, startY, endY)
                    }
                }

                STATE_DISAPPEAR -> {
                    if (currentTime() - item.lastTime >= item.alphaDisappearDuration) {
                        Log.e(TAG, "zfang, time, disappearDuration = ${currentTime() - item.startTime - item.alphaAppearDuration - item.middleDuration}," +
                                " total = ${currentTime() - item.startTime}")
                        item.state = STATE_OVER_LIFE
                        item.currentAlpha = 0f
                        continue
                    }
                    if (item.currentX <= -item.outOffsetX) {
                        elementsToRemove.add(item)
                        continue
                    }
                    val fraction = ((currentTime() - item.lastTime) / item.alphaDisappearDuration.toDouble()).toFloat()
                    if (fraction - item.lastFraction < 0.01) {
                        break
                    }
                    item.run {
                        if (-1f == startX) {
                            startX = pointsArray[2][0]
                            endX = pointsArray[3][0]
                            startY = pointsArray[2][1]
                            endY = pointsArray[3][1]
                        }

                        computeCurrentPoint(this, fraction, startX, endX, startY, endY)
                        currentAlpha = 1.0f - fraction
                    }
                }
            }
        }

        runningRainList.removeAll(elementsToRemove)
    }

    private fun computeCurrentPoint(item: PacketItem, fraction: Float, startX: Float, endX: Float, startY: Float, endY: Float) {
        item.apply {
            val offsetX = startX - endX
            val offsetY = endY - startY
            currentY = startY + fraction * offsetY
            currentX = startX - fraction * offsetX
            Log.d(TAG, "fraction = ${fraction}, currentX = $currentX, currentY = $currentY")
        }
    }

    private fun calculate() {
        for (item in packetList) {
            if (0L == item.startTime) {
                item.startTime = System.currentTimeMillis()
            }

            var fraction = ((System.currentTimeMillis().toDouble() - item.startTime) / item.middleDuration.toDouble()).toFloat()
            if (fraction > 1) {
                fraction = 1f
            }

            item.currentX = item.startX + fraction * (item.endX - item.startX)
            item.currentY = item.startY + fraction * (item.endY - item.startY)
        }
    }

    private fun calculateAlpha() {

    }
    private fun calculateDisappearAlpha() {

    }
    private fun calculate2() {
        for (item in packetList) {
            if (0L == item.startTime) {
                item.startTime = System.currentTimeMillis()
            }

            var fraction = ((System.currentTimeMillis().toDouble() - item.startTime) / item.middleDuration.toDouble()).toFloat()
            if (fraction > 1) {
                fraction = 1f
            }

            item.currentX = item.startX + fraction * (item.endX - item.startX)
            item.currentY = item.startY + fraction * (item.endY - item.startY)
        }
    }

    interface RainAnimationEnd {
        fun onEnd()
    }

    class PacketItemEvaluator: TypeEvaluator<PacketItem> {
        override fun evaluate(fraction: Float, startValue: PacketItem?, endValue: PacketItem?): PacketItem {
            return PacketItem().apply {
                when (startValue!!.state) {
                    STATE_APPEAR -> {
                        currentX = startValue.startX - fraction * (startValue.startX - startValue.endX)
                        currentY = startValue.startY + fraction * (startValue.endY - startValue.startY)
                        currentAlpha = fraction
                    }
                    STATE_MIDDLE -> {
                        currentX = startValue.startX - fraction * (startValue.startX - startValue.endX)
                        currentY = startValue.startY + fraction * (startValue.endY - startValue.startY)
                    }
                    STATE_DISAPPEAR -> {
                        currentX = startValue.startX - fraction * (startValue.startX - startValue.endX)
                        currentY = startValue.startY + fraction * (startValue.endY - startValue.startY)
                        currentAlpha = 1f - fraction
                    }
                }
            }
        }
    }
}