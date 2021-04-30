package com.zfang.graphicdemo.utils

import android.content.Context

fun getScreenHeight(ctx: Context): Int {
    val display = ctx.resources.displayMetrics;
    return display.heightPixels;
}

fun getScreenWidth(ctx: Context): Int {
    val display = ctx.resources.displayMetrics;
    return display.widthPixels;
}