package com.zfang.graphicdemo.utils

import android.content.Context
import android.view.View

fun getScreenHeight(ctx: Context): Int {
    val display = ctx.resources.displayMetrics;
    return display.heightPixels;
}

fun getScreenWidth(ctx: Context): Int {
    val display = ctx.resources.displayMetrics;
    return display.widthPixels;
}

/**
 * 获取状态栏高度
 * @return
 */
fun View.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelOffset(resourceId)
    }
    return result
}