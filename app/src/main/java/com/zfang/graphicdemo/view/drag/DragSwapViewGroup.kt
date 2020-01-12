package com.zfang.graphicdemo.view.drag

import android.animation.*
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.AnticipateOvershootInterpolator
import androidx.appcompat.widget.AppCompatImageView
import androidx.collection.SparseArrayCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.zfang.graphicdemo.R

class DragSwapViewGroup(ctx: Context, attrs: AttributeSet): ConstraintLayout(ctx, attrs) {
    /**
     * The shadow to be drawn above the [.draggedItem].
     */
    private var dragTopShadowDrawable: Drawable? = null
    /**
     * The shadow to be drawn below the [.draggedItem].
     */
    private var dragBottomShadowDrawable: Drawable? = null
    private var dragShadowHeight = 0
    private var touchSlop = 0

    private var downX = 0f
    private var downY = 0f

    private var lastX = 0f
    private var lastY = 0f

    private var dragView: View? = null
    private var viewBounds = Rect()
    private var totalDragOffsetX = 0f
    private var totalDragOffsetY = 0f
    private var swapViewOffsetX = 0f
    private var swapViewOffsetY = 0f
    private var dragDrawable: BitmapDrawable? = null
    private var strokeDrawable: Drawable? = null
    private val childCenterArray = SparseArrayCompat<ItemViewObj>()
    private var nearestViewObj: ItemViewObj? = null
    private var swapViewObj: ItemViewObj? = null
    private var swapBitmapDrawable: BitmapDrawable? = null
    private var currentCenter = Point()

    private var settleSwapAnimatorSet: AnimatorSet? = null
    private var settleDragAnimatorSet: AnimatorSet? = null

    private var swapListener: OnViewSwapListener? = null

    init {
        val resources = resources
        dragTopShadowDrawable = ContextCompat.getDrawable(context, R.drawable.ab_solid_shadow_holo_flipped)
        dragBottomShadowDrawable = ContextCompat.getDrawable(context, R.drawable.ab_solid_shadow_holo)
        dragShadowHeight = resources.getDimensionPixelSize(R.dimen.downwards_drop_shadow_height)
        strokeDrawable = ContextCompat.getDrawable(context, R.drawable.shape_bg_editaccount_avatar_stroke)
        touchSlop = ViewConfiguration.get(ctx).scaledTouchSlop
    }

    public fun setSwapListener(swapListener: OnViewSwapListener) {
        this.swapListener = swapListener
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        gatherViewCenter()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev!!.action) {
            MotionEvent.ACTION_DOWN -> {
                endPrevAnimation()
                downX = ev.x
                downY = ev.y
                lastX = downX
                lastY = downY
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaX = ev.x - downX
                val deltaY = ev.y - downY
                if (checkTouchSlop(deltaX, deltaY)) {
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                findTouchView(event)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaX = event.x - downX
                val deltaY = event.y - downY

                if (null != dragView/* && checkTouchSlop(deltaX, deltaY)*/) {
                    totalDragOffsetX = deltaX
                    totalDragOffsetY = deltaY
                    val centerX = ((dragView!!.left + dragView!!.right) / 2 + totalDragOffsetX).toInt()
                    val centerY = ((dragView!!.top + dragView!!.bottom) / 2 + totalDragOffsetY).toInt()
                    currentCenter.set(centerX, centerY)
                    Log.e("zfang", "centerX = $centerX, centerY = $centerY")
                    nearestViewObj = findNearestView(currentCenter)
                    swapViewObj = nearestViewObj
                    invalidate()
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                doActionUp()
            }
            MotionEvent.ACTION_UP -> {
                doActionUp()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        dragDrawable?.apply {
            canvas?.apply {
                canvas.save()

                Log.e("zfang", "totalDragOffsetX = $totalDragOffsetX, totalDragOffsetY = $totalDragOffsetY")
                canvas.translate(totalDragOffsetX, totalDragOffsetY)
                dragDrawable!!.draw(canvas)

                val left: Int = dragDrawable!!.getBounds().left
                val right: Int = dragDrawable!!.getBounds().right
                val top: Int = dragDrawable!!.getBounds().top
                val bottom: Int = dragDrawable!!.getBounds().bottom

                dragBottomShadowDrawable!!.setBounds(left, bottom, right, bottom + dragShadowHeight)
                dragBottomShadowDrawable!!.draw(canvas)

                if (null != dragTopShadowDrawable) {
                    dragTopShadowDrawable!!.setBounds(left, top - dragShadowHeight, right, top)
                    dragTopShadowDrawable!!.draw(canvas)
                }

                strokeDrawable?.apply {
                    strokeDrawable!!.setBounds(left, top, right, bottom)
                    strokeDrawable!!.draw(canvas)
                }

                canvas.restore()

                strokeDrawable?.apply {
                    if (null != nearestViewObj) {
                        val left = nearestViewObj!!.view.left
                        val top = nearestViewObj!!.view.top
                        val right = nearestViewObj!!.view.right
                        val bottom = nearestViewObj!!.view.bottom
                        strokeDrawable!!.setBounds(left, top, right, bottom)
                        strokeDrawable!!.draw(canvas)
                    }
                }
            }
        }

        swapBitmapDrawable?.apply {
            canvas?.apply {
                //draw swap to dragView
                canvas.save()
                canvas.translate(swapViewOffsetX, swapViewOffsetY)
                swapBitmapDrawable!!.draw(canvas)
                canvas.restore()
            }
        }
    }

    private fun gatherViewCenter() {
        for (index in 0 until  childCount) {
            val childView = getChildAt(index)
            val point = Point((childView.left + childView.right) / 2, (childView.top + childView.bottom) / 2)
            childCenterArray.put(index, ItemViewObj(point = point, view = childView))
        }
    }

    private fun findNearestView(centerPoint: Point): ItemViewObj {
        var nearestView: ItemViewObj? = null
        var dist = Int.MAX_VALUE
        for (index in 0 until childCount) {
            val itemViewObj = childCenterArray.valueAt(index)
            if (itemViewObj.view == dragView) {
                continue
            }
            if (null != nearestView) {
                val distTmp = itemViewObj.distanceTo(centerPoint)
                if (distTmp < dist) {
                    dist = distTmp
                    nearestView = itemViewObj
                }
            } else {
                dist = itemViewObj.distanceTo(centerPoint)
                nearestView = itemViewObj
            }
        }

        return nearestView!!
    }

    private fun doActionUp() {
//        settleOrigin()
        doSwap()
        swap2Origin()
    }

    private fun endPrevAnimation() {
        settleSwapAnimatorSet?.apply {
            if (isRunning) {
                end()
            }
        }
        settleDragAnimatorSet?.apply {
            if (isRunning) {
                end()
            }
        }
    }

    //滑动到要交换的view  dragView -> swapView
    private fun doSwap() {
        swapViewObj?.apply {
            val swapView = swapViewObj!!.view
            val settleSwapX = ValueAnimator.ofFloat(totalDragOffsetX, swapView.left.toFloat() - dragView!!.left)
            settleSwapX.addUpdateListener(ValueAnimator.AnimatorUpdateListener {
                totalDragOffsetX = it.getAnimatedValue() as Float
                invalidate()
            })
            val settleSwapY = ValueAnimator.ofFloat(totalDragOffsetY, swapView.top.toFloat() - dragView!!.top)
            settleSwapY.addUpdateListener {
                totalDragOffsetY = it.getAnimatedValue() as Float
                invalidate()
            }

            settleSwapAnimatorSet = AnimatorSet()
            settleSwapAnimatorSet?.apply {
                duration = 500
                interpolator = AnticipateOvershootInterpolator()
                play(settleSwapX).with(settleSwapY)
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        swapView.apply {
                            (swapView as AppCompatImageView).setImageDrawable(dragDrawable)
                            dragDrawable = null
                            invalidate()
                        }
                    }
                })

                start()
            }
        }
    }

    //滑动到原始位置swapView -> dragView
    private fun swap2Origin() {
        swapViewObj?.apply {
            val swapView = swapViewObj!!.view
            swapBitmapDrawable = getDragDrawable(swapView)
            val settleSwapX = ValueAnimator.ofFloat(0f, dragView!!.left - swapView.left.toFloat())
            settleSwapX.addUpdateListener(ValueAnimator.AnimatorUpdateListener {
                swapViewOffsetX = it.getAnimatedValue() as Float
                invalidate()
            })
            val settleSwapY = ValueAnimator.ofFloat(0f, dragView!!.top - swapView.top.toFloat())
            settleSwapY.addUpdateListener {
                swapViewOffsetY = it.getAnimatedValue() as Float
                invalidate()
            }

            settleDragAnimatorSet = AnimatorSet()
            settleDragAnimatorSet?.apply {
                duration = 500
                interpolator = AnticipateOvershootInterpolator()
                play(settleSwapX).with(settleSwapY)
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        dragView?.apply {
                            (dragView as AppCompatImageView).setImageDrawable(swapBitmapDrawable)
                            swapBitmapDrawable = null
                            invalidate()
                            dragView!!.visibility = View.VISIBLE
                            notifySwap()
                        }
                    }
                })

                start()
            }
        }
    }

    private fun notifySwap() {
        swapListener?.apply {
            onViewSwap(dragView!!, indexOfChild(dragView), swapViewObj!!.view, indexOfChild(swapViewObj!!.view))
        }
    }

    private fun settleOrigin() {
        val settleOriginX = ValueAnimator.ofFloat(totalDragOffsetX, 0f)
        settleOriginX.addUpdateListener(ValueAnimator.AnimatorUpdateListener {
            totalDragOffsetX = it.getAnimatedValue() as Float
            invalidate()
        })
        val settleOriginY = ValueAnimator.ofFloat(totalDragOffsetY, 0f)
        settleOriginY.addUpdateListener {
            totalDragOffsetY = it.getAnimatedValue() as Float
            invalidate()
        }
        val animatorSet = AnimatorSet()
        animatorSet.duration = 500
        animatorSet.interpolator = AnticipateOvershootInterpolator()
        animatorSet.play(settleOriginX).with(settleOriginY)
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                dragView?.apply {
                    dragDrawable = null
                    invalidate()
                    dragView!!.visibility = View.VISIBLE
                }
            }
        })

        animatorSet.start()
    }

    private fun findTouchView(event: MotionEvent): Boolean {
        var flag = false
        for (childIndex in childCount -1 downTo  0) {
            val childView = getChildAt(childIndex)
            viewBounds.set(childView.left, childView.top, childView.right, childView.bottom)
            if (viewBounds.contains(event.x.toInt(), event.y.toInt())) {
                dragView = childView
                dragDrawable = getDragDrawable(dragView!!)
                dragView!!.visibility = View.INVISIBLE
                flag = true
                totalDragOffsetX = 0f
                totalDragOffsetY = 0f
                break
            }
        }

        return flag
    }

    private fun checkTouchSlop(deltaX: Float, deltaY: Float): Boolean {
        val exponent = 2.toDouble()
        return (Math.abs(deltaX) > touchSlop) || (Math.abs(deltaY) > touchSlop)
                || (Math.sqrt(Math.pow(deltaX.toDouble(), exponent) + Math.pow(deltaY.toDouble(), exponent)) > touchSlop)
    }

    private fun getDragDrawable(view: View): BitmapDrawable? {
        val top = view.top
        val left = view.left
        val bitmap = getBitmapFromView(view)
        val drawable = BitmapDrawable(resources, bitmap)
        drawable.bounds = Rect(left, top, left + view.width, top + view.height)
        return drawable
    }

    /**
     * @return a bitmap showing a screenshot of the view passed in.
     */
    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }


    private class ItemViewObj(val point: Point, val view: View) {
        fun distanceTo(to: Point): Int {
            val dx = (to.x - point.x).toDouble()
            val dy = (to.y - point.y).toDouble()
            return Math.sqrt(Math.pow(dx, 2.toDouble()) + Math.pow(dy, 2.toDouble())).toInt()
        }
    }

    interface OnViewSwapListener {
        fun onViewSwap(firstView: View, firstPosition: Int, secondView: View, secondPosition: Int)
    }
}