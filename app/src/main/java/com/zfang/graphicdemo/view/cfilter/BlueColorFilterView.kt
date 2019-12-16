package com.zfang.graphicdemo.view.cfilter

import android.content.Context
import android.util.AttributeSet

class BlueColorFilterView(context: Context?, attrs: AttributeSet?) :
    BaseColorFilterView(context, attrs) {
    override fun getColorMatrix(): FloatArray {
        return floatArrayOf(
            0f, 0f, 0f, 0f, 0f,//R
            0f, 0f, 0f, 0f, 0f,//G
            0f, 0f, 1f, 0f, 0f,//B
            0f, 0f, 0f, 1f, 0f //A
        )
    }
}