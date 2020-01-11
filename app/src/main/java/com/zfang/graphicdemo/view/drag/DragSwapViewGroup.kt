package com.zfang.graphicdemo.view.drag

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.LinearLayout
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


    init {
        val resources = resources
        dragTopShadowDrawable = ContextCompat.getDrawable(context, R.drawable.ab_solid_shadow_holo_flipped)
        dragBottomShadowDrawable = ContextCompat.getDrawable(context, R.drawable.ab_solid_shadow_holo)
        dragShadowHeight = resources.getDimensionPixelSize(R.dimen.downwards_drop_shadow_height)
        touchSlop = ViewConfiguration.get(ctx).scaledTouchSlop
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }
}