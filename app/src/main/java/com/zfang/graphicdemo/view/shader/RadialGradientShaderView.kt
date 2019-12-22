package com.zfang.graphicdemo.view.shader

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.RadialGradient
import android.graphics.Shader
import android.util.AttributeSet

class RadialGradientShaderView(context: Context?, attrs: AttributeSet?) :
    BaseShaderView(context, attrs) {
    override fun getShader(left: Float, top: Float, right: Float, bottom: Float): Shader {
        var radius = 0f
        if ((right - left) > (bottom - top)) {
            radius = (right - left) / 2
        } else {
            radius = (bottom - top) / 2
        }
//        val radius =  ?: (right - left) / 2 : (bottom - top) / 2
        return RadialGradient((left + right) / 2, (top + bottom) / 2, radius, Color.RED, Color.BLUE, Shader.TileMode.CLAMP)
    }

    override fun getShaderCircle(left: Float, top: Float, right: Float, bottom: Float): Shader {
        var radius = 0f
        if ((right - left) > (bottom - top)) {
            radius = (right - left) / 2
        } else {
            radius = (bottom - top) / 2
        }
        return RadialGradient((left + right) / 2, (top + bottom) / 2, radius, Color.RED, Color.BLUE, Shader.TileMode.CLAMP)
    }


    override fun getShaderPath(left: Float, top: Float, right: Float, bottom: Float): Shader {
        var radius = 0f
        if ((right - left) > (bottom - top)) {
            radius = (right - left) / 2
        } else {
            radius = (bottom - top) / 2
        }
        return RadialGradient((left + right) / 2, (top + bottom) / 2, radius, Color.RED, Color.BLUE, Shader.TileMode.CLAMP)
    }
}