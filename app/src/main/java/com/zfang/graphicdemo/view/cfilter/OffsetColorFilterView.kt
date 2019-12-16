package com.zfang.graphicdemo.view.cfilter

import android.content.Context
import android.util.AttributeSet

class OffsetColorFilterView(context: Context?, attrs: AttributeSet?) :
    BaseColorFilterView(context, attrs) {
    override fun getColorMatrix(): FloatArray {
        return floatArrayOf(
            1f, 0f, 0f, 0f, 8f,//R
            0f, 2f, 0f, 0f, 8f,//G
            0f, 0f, 3f, 0f, 8f,//B
            0f, 0f, 0f, 1f, 0f //A
        )
    }
}