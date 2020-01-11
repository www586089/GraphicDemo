package com.zfang.graphicdemo.view.drag

import android.animation.*
import android.content.Context
import android.graphics.Rect
import android.os.HandlerThread
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.AnimationSet
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import androidx.core.animation.addListener
import androidx.core.view.ViewCompat

class DragViewGroup(context: Context, attributeSet: AttributeSet): FrameLayout(context, attributeSet) {

    private var mDownX = 0f
    private var mDownY = 0f
    private var mDragViewOriginLeft = 0
    private var mDragViewOriginTop = 0
    private var mDragViewOriginX = 0f
    private var mDragViewOriginY = 0f
    private var mDragViewOriginRight = 0f
    private var mDragViewOriginBottom = 0f
    private var mLastX = 0f
    private var mLastY = 0f
    private var touchSlop: Int

    private var dragView: View? = null
    private var viewBounds = Rect()

    enum class DragState {
        None,
        Draging,
        Idle,
    }
    private var dragState = DragState.None

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = event.x
                mLastY = event.y
                dragState = DragState.None
                if (findTouchView(event)) {
                    mDownX = mLastX
                    mDownY = mLastY
                    dragState = DragState.Draging
                }
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaX = event.x - mLastX
                val deltaY = event.y - mLastY

                mLastX = event.x
                mLastY = event.y
                if (DragState.Draging == dragState && checkTouchSlop(deltaX = deltaX, deltaY = deltaY)) {
                    dragView.apply {
                        ViewCompat.offsetLeftAndRight(dragView!!, deltaX.toInt())
                        ViewCompat.offsetTopAndBottom(dragView!!, deltaY.toInt())
                    }
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                doDragEnd()
            }
            MotionEvent.ACTION_UP -> {
                doDragEnd()
            }
        }
        return super.onTouchEvent(event)
    }

    fun doDragEnd() {
        if (DragState.Draging == dragState) {
            animateToOrigin()
        }
    }

    private var lastX = 0
    private var lastY = 0
    fun animateToOrigin() {
//        val handlerThreadX = object : HandlerThread("x") {
//            override fun onLooperPrepared() {
//                val fromX = dragView!!.x.toInt()
//                val toX = mDragViewOriginX.toInt()
//                for (i in fromX downTo toX) {
//                    dragView!!.post {
//                        dragView!!.offsetLeftAndRight(-1)
//                    }
//                    Thread.sleep(1)
//                }
//            }
//        }
//        val handlerThreadY = object : HandlerThread("y") {
//            override fun onLooperPrepared() {
//                val fromX = dragView!!.y.toInt()
//                val toX = mDragViewOriginY.toInt()
//                for (i in fromX downTo toX) {
//                    dragView!!.post {
//                        dragView!!.offsetTopAndBottom(-1)
//                    }
//                    Thread.sleep(1)
//                }
//            }
//        }
//        handlerThreadX.start()
//        handlerThreadY.start()
        dragView?.apply {
            val animatorX = ValueAnimator.ofInt(dragView!!.x.toInt(), mDragViewOriginX.toInt())
            animatorX.addUpdateListener {
                val currentX = it.getAnimatedValue() as Int
                Log.e("zfang", "x = $currentX")
                ViewCompat.offsetLeftAndRight(dragView!!, currentX)
                lastX = currentX
            }
            val animatorY = ValueAnimator.ofInt(dragView!!.y.toInt(), mDragViewOriginY.toInt())
            animatorY.addUpdateListener {
                val currentY = it.getAnimatedValue() as Int
                Log.e("zfang", "x = $currentY")
                ViewCompat.offsetTopAndBottom(dragView!!, currentY)
                lastY = currentY
            }
            val animationSet = AnimatorSet()
            animationSet.play(animatorX).with(animatorY)
            animationSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    Log.e("zfang", "mDragViewOriginLeft = $mDragViewOriginLeft, mDragViewOriginTop = $mDragViewOriginTop")
                    dragState = DragState.None
                    dragView = null
                }
            })
            animationSet.start()
        }
    }

    fun checkTouchSlop(deltaX: Float, deltaY: Float): Boolean {
        val exponent = 2.toDouble()
        return (Math.abs(deltaX) > touchSlop) || (Math.abs(deltaY) > touchSlop)
                || (Math.sqrt(Math.pow(deltaX.toDouble(), exponent) + Math.pow(deltaY.toDouble(), exponent)) > touchSlop)
    }

    fun findTouchView(event: MotionEvent): Boolean {
        var flag = false
        for (childIndex in childCount -1 downTo  0) {
            val childView = getChildAt(childIndex)
            viewBounds.set(childView.left, childView.top, childView.right, childView.bottom)
            if (viewBounds.contains(event.x.toInt(), event.y.toInt())) {
                dragView = childView
                mDragViewOriginX = childView.x
                mDragViewOriginY = childView.y
                mDragViewOriginRight = childView.right.toFloat()
                mDragViewOriginBottom = childView.bottom.toFloat()
                mDragViewOriginLeft = childView.left
                mDragViewOriginTop = childView.top
                flag = true
                break
            }
        }

        return flag && dragState != DragState.Draging
    }
}