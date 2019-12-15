package com.zfang.graphicdemo.common

import android.content.Context

fun Int.px2Dp(context: Context): Int {
    return (this * context.resources.displayMetrics.density + 0.5f).toInt()
}

fun Float.dec2(): String = String.format("%.2f", this)