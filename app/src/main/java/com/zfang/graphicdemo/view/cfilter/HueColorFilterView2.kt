package com.zfang.graphicdemo.view.cfilter

import android.content.Context
import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.AttributeSet

/**
 * 调整色调
 * 0： 红色分量
 * 1： 绿色分量
 * 2： 蓝色分量
 */
class HueColorFilterView2(context: Context?, attrs: AttributeSet?) :
    BaseLuminanceColorFilterView(context, attrs) {

    private val colorMatrix = ColorMatrix()
    override fun getColorMatrix(): ArrayList<ColorMatrixColorFilter> {
        val colorMatrixs = ArrayList<ColorMatrixColorFilter>()
        for (i in 0..23) {
            if (i < 8) {
                colorMatrix.setRotate(0, (i * 50).toFloat())
            } else if (i < 16) {
                colorMatrix.setRotate(1, ((i % 8) * 50).toFloat())
            } else {
                colorMatrix.setRotate(1, ((i % 8) * 50).toFloat())
            }
            colorMatrixs.add(ColorMatrixColorFilter(colorMatrix))
        }

        return colorMatrixs
    }
}