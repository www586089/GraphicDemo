package com.zfang.graphicdemo.view.shader

import android.content.Context
import android.graphics.*
import android.util.AttributeSet

class SweepGradientShaderView(context: Context?, attrs: AttributeSet?) :
    BaseShaderView(context, attrs) {
    override fun getShader(left: Float, top: Float, right: Float, bottom: Float): Shader {
        return SweepGradient((left + right) / 2, (top + bottom) / 2, Color.RED, Color.BLUE)
    }

    override fun getShaderCircle(left: Float, top: Float, right: Float, bottom: Float): Shader {
        return SweepGradient((left + right) / 2, (top + bottom) / 2, Color.RED, Color.BLUE)
    }

    override fun getShaderPath(left: Float, top: Float, right: Float, bottom: Float): Shader {
        return SweepGradient((left + right) / 2, (top + bottom) / 2, Color.RED, Color.BLUE)
    }
}