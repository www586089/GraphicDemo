package com.zfang.graphicdemo.view.shader

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet

class LinearGradientShaderView(context: Context?, attrs: AttributeSet?) :
    BaseShaderView(context, attrs) {
    override fun getShader(left: Float, top: Float, right: Float, bottom: Float): Shader {
        return LinearGradient(left, top, right, top, Color.RED, Color.GREEN, Shader.TileMode.CLAMP)
    }

    override fun getShaderCircle(left: Float, top: Float, right: Float, bottom: Float): Shader {
        return LinearGradient(left, top, right, top, Color.RED, Color.GREEN, Shader.TileMode.MIRROR)
    }

    override fun getShaderPath(left: Float, top: Float, right: Float, bottom: Float): Shader {
        return LinearGradient(left, top, right, top, Color.RED, Color.GREEN, Shader.TileMode.MIRROR)
    }
}