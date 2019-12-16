package com.zfang.graphicdemo.view.cfilter

import android.content.Context
import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.AttributeSet

/**
 * 第1行决定红色值R
 * 第2行决定绿色值G
 * 第3行决定蓝色值B
 * 第4行决定透明度A
 * 第5列是颜色偏移量
 */
class LuminanceColorFilterView(context: Context?, attrs: AttributeSet?) :
    BaseLuminanceColorFilterView(context, attrs) {

    private val colorMatrix = ColorMatrix()
    override fun getColorMatrix(): ArrayList<ColorMatrixColorFilter> {
        val colorMatrixs = ArrayList<ColorMatrixColorFilter>()
        for (i in 0..23) {
            if (i < 8) {
                colorMatrix.setScale(i * .1f, i * .1f, i * .1f, i * .1f);
            } else if (i < 16) {
                colorMatrix.setScale(i * .1f, i * .1f, i * .1f, i * .1f);
            } else {
                colorMatrix.setScale(i * .1f, i * .1f, i * .1f, i * .1f);
            }
            colorMatrixs.add(ColorMatrixColorFilter(colorMatrix))
        }

        return colorMatrixs
    }
}