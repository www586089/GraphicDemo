package com.zfang.graphicdemo.view.test

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.zfang.graphicdemo.common.px2Dp
import com.zfang.graphicdemo.utils.getScreenHeight
import com.zfang.graphicdemo.utils.getStatusBarHeight

class HollowGuideView(val ctx: Context, attrs: AttributeSet) : ViewGroup(ctx, attrs) {

    private val TAG = "HollowGuideView"
    private val maskColor = Color.parseColor("#90000000") //半透明
    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    private val anchorRectF = RectF()
    private val clickRectF = RectF()
    private var clickView: View? = null
    private var guideInfo: GuideInfo? = null
    private var guideInfoHelper: GuideInfoHelper? = null

    init {
        setWillNotDraw(false)
        mPaint.color = Color.WHITE
        mPaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            val layerId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
            } else {
                canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
            }
            canvas.drawColor(maskColor)//dst
            mPaint.xfermode = xfermode
            val roundCornerRadius = guideInfo?.cornerRadius ?: 0f
            canvas.drawRoundRect(anchorRectF, roundCornerRadius, roundCornerRadius, mPaint)//src
            mPaint.xfermode = null

            canvas.restoreToCount(layerId)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        guideInfo ?: return
        if (childCount > 0) {
            val childView = getChildAt(0) as ViewGroup
            val anchorLeft = anchorRectF.left.toInt()
            val anchorCenterY = (anchorRectF.top + anchorRectF.height() / 2).toInt()
            val fakeAnchorCenterX = (anchorLeft + anchorRectF.width() * guideInfo!!.gravity).toInt()
            layoutChild(childView, anchorLeft, anchorCenterY, fakeAnchorCenterX)
        }
    }

    private fun layoutChild(childView: ViewGroup, anchorLeft: Int, anchorCenterY: Int, fakeAnchorCenterX: Int) {
        guideInfo ?: return
        val left: Int
        val top: Int
        val right: Int
        val bottom: Int
        if (TO_ANCHOR_LEFT == guideInfo!!.toAnchorDirection || TO_ANCHOR_RIGHT == guideInfo!!.toAnchorDirection) {
            val offsetY = guideInfo!!.let { (childView.measuredHeight * it.scale).toInt() }
            left = anchorLeft - childView.measuredWidth
            top = anchorCenterY - offsetY
            right = anchorLeft
            bottom = anchorCenterY - offsetY + childView.measuredHeight
        } else {
            val offsetX = if (TO_ANCHOR_BOTTOM == guideInfo!!.toAnchorDirection || TO_ANCHOR_TOP == guideInfo!!.toAnchorDirection){
                guideInfo!!.let { (childView.measuredWidth * it.scale).toInt() }
            } else {
                0
            }
            right = fakeAnchorCenterX + offsetX
            left = right - childView.measuredWidth
            top = anchorRectF.bottom.toInt()
            bottom = top + childView.measuredHeight
        }
        doLayoutChild(childView, left, top, right, bottom)
    }

    private fun doLayoutChild(childView: ViewGroup, left: Int, top: Int, right: Int, bottom: Int) {
        childView.layout(left, top, right, bottom)
        clickView?.apply {
            clickRectF.set(x, y, x + measuredWidth, y + measuredHeight)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        guideInfo ?: return
        Log.d(TAG, "width = ${measuredWidth}, height = ${measuredHeight}, screenHeight = ${getScreenHeight(ctx)}")
        if (childCount > 0) {
            val childView = getChildAt(0) as ViewGroup
            measureChildren(widthMeasureSpec, heightMeasureSpec)
            val anchorLeft = anchorRectF.left.toInt()
            val anchorCenterY = (anchorRectF.top + anchorRectF.height() / 2).toInt()
            val fakeAnchorCenterX = (anchorLeft + anchorRectF.width() * guideInfo!!.gravity).toInt()
            Log.d(TAG, "childWidth = ${childView.measuredWidth}, childHeight = ${childView.measuredHeight}")
            measureAnchorContentView(childView, anchorLeft, anchorCenterY, fakeAnchorCenterX)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return super.onTouchEvent(event)
        if (clickRectF.contains(event.x, event.y)) {
            return super.onTouchEvent(event)
        } else {
            return true
        }
    }


    private fun measureAnchorContentView(childView: ViewGroup, anchorLeft: Int, anchorCenterY: Int, fakeAnchorCenterX: Int) {
        guideInfo ?: return
        //在锚点的左边、右边
        if (TO_ANCHOR_LEFT == guideInfo!!.toAnchorDirection || TO_ANCHOR_RIGHT == guideInfo!!.toAnchorDirection) {
            var left = anchorLeft - childView.measuredWidth
            //距离左边至少26dp
            if (left < 26.px2Dp(ctx)) {
                left = 26.px2Dp(ctx)
                val width = (anchorLeft - left)
                val scale = width.toFloat() / childView.measuredWidth
                val height = (scale * childView.measuredHeight).toInt()
                doMeasureNestedChildren(childView, width, height, scale)
            }
        } else {//在锚点的上边、下边
            val offsetX = if (TO_ANCHOR_BOTTOM == guideInfo!!.toAnchorDirection || TO_ANCHOR_TOP == guideInfo!!.toAnchorDirection) {
                guideInfo!!.let { (childView.measuredWidth * it.scale).toInt() }
            } else {
                0
            }
            val right = fakeAnchorCenterX + offsetX
            val left = right - childView.measuredWidth

            var width = childView.measuredWidth
            var height = childView.measuredHeight
            var scale = 1f
            //距离左右至少50dp
            if (left < 24.px2Dp(ctx)) {
                width -= 24.px2Dp(ctx)
                scale = (width.toFloat() / childView.measuredWidth)
                height = (height * scale).toInt()
            } else if (right > measuredWidth - 24.px2Dp(ctx)) {
                width -= (right - (measuredWidth - 24.px2Dp(ctx)))
                scale = (width.toFloat() / childView.measuredWidth)
                height = (height * scale).toInt()
            }

            doMeasureNestedChildren(childView, width, height, scale)
        }
    }

    private fun doMeasureNestedChildren(childView: ViewGroup, width: Int, height: Int, scale: Float) {
        val pWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        val pHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        val nestChildCount = childView.childCount
        for (index in 0 until nestChildCount) {
            Log.d(TAG, "index = $index, scale = $scale")
            val child = childView.getChildAt(index)
            val LP = child.layoutParams
            LP.width = (child.measuredWidth * scale).toInt()
            LP.height = (child.measuredHeight * scale).toInt()
            Log.d(TAG, "child, width = ${LP.width}, height = ${LP.height}")
        }
        measureChildren(pWidthMeasureSpec, pHeightMeasureSpec)
        Log.d(TAG, "after measure, childView.width = ${childView.measuredWidth}, height = ${childView.measuredHeight}")
        for (index in 0 until nestChildCount) {
            val child = childView.getChildAt(index)
            Log.d(TAG, "index = ${index}, child, measuredWidth = ${child.measuredWidth}, measuredWidth = ${child.measuredHeight}")
        }
    }

    private fun setDisplayHollowGuidView(anchorView: View, clickView: View, guideView: View) {
        val x = anchorView.x
        val y = anchorView.y
        this.clickView = clickView
        val statusBarHeight = getStatusBarHeight();
        Log.d(TAG, "x = ${x}, y = ${y}, height = ${anchorView.height}, width = ${anchorView.width}, statusHeight = ${statusBarHeight}")
        Log.d(TAG, "guideView.width = ${guideView.width}, guideView.height = ${guideView.height}")
        anchorRectF.set(x, y, x + anchorView.width, y + anchorView.height)
        clickRectF.set(clickView.x, clickView.y, clickView.x + clickView.width, clickView.y + clickView.height)
        val LP = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        addView(guideView, LP)
        requestLayout()
    }

    fun isComplete(): Boolean {
        return false
    }

    fun showNext() {
        if (guideInfoHelper?.hasNext() == true) {
            removeAllViews()
            this.guideInfo = guideInfoHelper?.getNext()
            guideInfo?.let { setDisplayHollowGuidView(it.anchorView, it.clickView, it.guideViewContent) }
            requestLayout()
        } else {
            Toast.makeText(ctx, "没有下一步了", Toast.LENGTH_SHORT).show()
        }
    }

    fun showGuide(guideInfoHelper: GuideInfoHelper) {
        visibility = VISIBLE
        this.guideInfoHelper = guideInfoHelper
        this.guideInfo = guideInfoHelper.getNext()
        guideInfo?.let { setDisplayHollowGuidView(it.anchorView, it.clickView, it.guideViewContent) }
        requestLayout()
    }


    fun completeGuide() {
        guideInfoHelper?.clearGuideInfo()
        removeAllViews()
        visibility = GONE
    }
}