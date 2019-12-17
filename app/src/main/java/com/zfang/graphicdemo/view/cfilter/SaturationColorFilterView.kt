package com.zfang.graphicdemo.view.cfilter

import android.content.Context
import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.AttributeSet

/**
 * 调整饱和度
 * 0： 灰色
 * 1： 正常
 */
class SaturationColorFilterView(context: Context?, attrs: AttributeSet?) :
    BaseLuminanceColorFilterView(context, attrs) {

    private val colorMatrix = ColorMatrix()
    override fun getColorMatrix(): ArrayList<ColorMatrixColorFilter> {
        val colorMatrixs = ArrayList<ColorMatrixColorFilter>()
        for (i in 0..23) {
            if (i < 8) {
                colorMatrix.setSaturation(i * 0.2f)
            } else if (i < 16) {
                colorMatrix.setSaturation(i * 0.5f)
            } else {
                colorMatrix.setSaturation(i * 2f)
            }
            colorMatrixs.add(ColorMatrixColorFilter(colorMatrix))
        }

        return colorMatrixs
    }
}