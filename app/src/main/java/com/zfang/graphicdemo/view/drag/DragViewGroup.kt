package com.zfang.graphicdemo.view.drag

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.core.view.ViewCompat

class DragViewGroup(context: Context, attributeSet: AttributeSet): FrameLayout(context, attributeSet) {

    private var mDownX = 0f
    private var mDownY = 0f
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
            dragState = DragState.None
            dragView = null
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
                flag = true
                break
            }
        }

        return flag && dragState != DragState.Draging
    }
}