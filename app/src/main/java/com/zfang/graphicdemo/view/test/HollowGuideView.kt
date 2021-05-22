package com.zfang.graphicdemo.view.test

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.common.px2Dp
import com.zfang.graphicdemo.utils.getScreenHeight
import com.zfang.graphicdemo.utils.getStatusBarHeight
import kotlinx.android.synthetic.main.activity_test.view.*

class HollowGuideView(val ctx: Context, attrs: AttributeSet) : ViewGroup(ctx, attrs) {

    private val TAG = "HollowGuideView"
    private val maskColor = Color.parseColor("#90000000")
    private var mHollowPoint: PointF = PointF(0f, 0f)
    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    private val rectF = RectF()
    private var roundCornerRadius = 5.px2Dp(ctx).toFloat()
    private var guideInfo: GuideInfo? = null

    init {
        setWillNotDraw(false)
        mPaint.color = Color.WHITE
        mPaint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            val layerId = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
            canvas.drawColor(maskColor)//dst
            mPaint.xfermode = xfermode
            canvas.drawRoundRect(rectF, roundCornerRadius, roundCornerRadius, mPaint)//src
            mPaint.xfermode = null

            canvas.restoreToCount(layerId)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount > 0) {
            val childView = getChildAt(0)
            val anchorLeft = rectF.left.toInt()
            val anchorCenterY = (rectF.top + rectF.height() / 2).toInt()
            val anchorCenterX = (anchorLeft + rectF.width() / 2).toInt()
            layoutChild(childView, anchorLeft, anchorCenterY, anchorCenterX)
        }
    }

    private fun layoutChild(childView: View, anchorLeft: Int, anchorCenterY: Int, anchorCenterX: Int) {
        guideInfo ?: return
        if (TO_ANCHOR_LEFT == guideInfo!!.toAnchorDirection || TO_ANCHOR_RIGHT == guideInfo!!.toAnchorDirection) {
            val offsetY = guideInfo!!.let { (childView.measuredHeight * it.scale).toInt() }
            var left = anchorLeft - childView.measuredWidth
            var top = anchorCenterY - offsetY
            var right = anchorLeft
            var bottom = anchorCenterY - offsetY + childView.measuredHeight
            childView.layout(left, top, right, bottom)
        } else {
            val offsetX = if (TO_ANCHOR_BOTTOM == guideInfo!!.toAnchorDirection || TO_ANCHOR_TOP == guideInfo!!.toAnchorDirection){
                guideInfo!!.let { (childView.measuredWidth * it.scale).toInt() }
            } else {
                0
            }
            var right = anchorCenterX + offsetX
            var left = right - childView.measuredWidth
            var top = rectF.bottom.toInt()
            var bottom = top + childView.measuredHeight
            childView.layout(left, top, right, bottom)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(TAG, "width = ${measuredWidth}, height = ${measuredHeight}, screenHeight = ${getScreenHeight(ctx)}")
        if (childCount > 0) {
            val childView = getChildAt(0) as ViewGroup
            measureChildren(widthMeasureSpec, heightMeasureSpec)
            val anchorLeft = rectF.left.toInt()
            val anchorCenterY = (rectF.top + rectF.height() / 2).toInt()
            val anchorCenterX = (anchorLeft + rectF.width() / 2).toInt()
            Log.d(TAG, "childWidth = ${childView.measuredWidth}, childHeight = ${childView.measuredHeight}")
            measureAnchorContentView(childView, anchorLeft, anchorCenterY, anchorCenterX)
        }
    }

    private fun measureAnchorContentView(childView: ViewGroup, anchorLeft: Int, anchorCenterY: Int, anchorCenterX: Int) {
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
        } else {
            val offsetX = if (TO_ANCHOR_BOTTOM == guideInfo!!.toAnchorDirection || TO_ANCHOR_TOP == guideInfo!!.toAnchorDirection){
                guideInfo!!.let { (childView.measuredWidth * it.scale).toInt() }
            } else {
                0
            }
            var right = anchorCenterX + offsetX
            var left = right - childView.measuredWidth

            var width = childView.measuredWidth
            var height = childView.measuredHeight
            var scale = 1f
            //距离左右至少50dp
            if (left < 50.px2Dp(ctx)) {
                width -= 50.px2Dp(ctx)
                scale = (width.toFloat() / childView.measuredWidth)
                height = (height * scale).toInt()
            } else if (right > measuredWidth - 50.px2Dp(ctx)) {
                width = width - (right - (measuredWidth - 50.px2Dp(ctx)))
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
            Log.d(TAG, "index = $index")
            val child = childView.getChildAt(index)
            val LP = child.layoutParams
            LP.width = (child.measuredWidth * scale).toInt()
            LP.height = (child.measuredHeight * scale).toInt()
            Log.d(TAG, "child, width = ${LP.width}, height = ${LP.height}")
        }
        measureChildren(pWidthMeasureSpec, pHeightMeasureSpec)
    }

    fun setDisplayHollowGuidView(view: View, guideView: View, guideInfo: GuideInfo) {
        this.guideInfo = guideInfo
        val x = view.x
        val y = view.y
        val statusBarHeight = getStatusBarHeight();
        Log.d(TAG, "x = ${x}, y = ${y}, height = ${view.height}, width = ${view.width}, statusHeight = ${statusBarHeight}")
        Log.d(TAG, "guideView.width = ${guideView.width}, guideView.height = ${guideView.height}")
        mHollowPoint.set(x + view.width / 2, y + view.height / 2)
        rectF.set(x, y, x + view.width, y + view.height)
        val LP = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        addView(guideView, LP)
        requestLayout()
    }

    fun isComplete(): Boolean {
        return false
    }

    fun showNext(view: View) {
        removeAllViews()
        val guideViewContent = LayoutInflater.from(ctx).inflate(R.layout.home_guide_layout_2, null)
        guideViewContent.findViewById<AppCompatTextView>(R.id.guide_home_know).setOnClickListener {
            removeAllViews()
            visibility = GONE
        }
        setDisplayHollowGuidView(view, guideViewContent, GuideInfo(TO_ANCHOR_BOTTOM, scale = 0.38f))
    }

}