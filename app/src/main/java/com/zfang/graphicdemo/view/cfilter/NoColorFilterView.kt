package com.zfang.graphicdemo.view.cfilter

import android.content.Context
import android.util.AttributeSet

/**
 * 第1行决定红色值R
 * 第2行决定绿色值G
 * 第3行决定蓝色值B
 * 第4行决定透明度A
 * 第5列是颜色偏移量
 */
class NoColorFilterView(context: Context?, attrs: AttributeSet?) :
    BaseColorFilterView(context, attrs) {
    override fun getColorMatrix(): FloatArray {
        return floatArrayOf(
            1f, 0f, 0f, 0f, 0f,//R
            0f, 1f, 0f, 0f, 0f,//G
            0f, 0f, 1f, 0f, 0f,//B
            0f, 0f, 0f, 1f, 0f //A
        )
    }
}