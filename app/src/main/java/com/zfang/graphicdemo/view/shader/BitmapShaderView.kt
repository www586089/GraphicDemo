package com.zfang.graphicdemo.view.shader

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.zfang.graphicdemo.R

class BitmapShaderView(context: Context?, attrs: AttributeSet?) :
    BaseShaderView(context, attrs) {
    var bitmap = BitmapFactory.decodeResource(context!!.resources, R.drawable.filter_icon_film2)

    var bitmapCircle = BitmapFactory.decodeResource(context!!.resources, R.drawable.filter_icon_fresh2)
    init {
        var oldWidth = bitmap.width.toFloat()
        var oldHeight = bitmap.height.toFloat()
        var scaleMatrix = Matrix()
        scaleMatrix.postScale(0.6f, 0.6f, oldWidth / 2, oldHeight / 2)

        bitmap = Bitmap.createBitmap(bitmap, 0, 0, oldWidth.toInt(), oldHeight.toInt(), scaleMatrix, true)


        oldWidth = bitmapCircle.width.toFloat()
        oldHeight = bitmapCircle.height.toFloat()
        scaleMatrix = Matrix()
        scaleMatrix.postScale(0.6f, 0.6f, oldWidth / 2, oldHeight / 2)

        bitmapCircle = Bitmap.createBitmap(bitmapCircle, 0, 0, oldWidth.toInt(), oldHeight.toInt(), scaleMatrix, true)
    }
    override fun getShader(left: Float, top: Float, right: Float, bottom: Float): Shader {
        return BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
    }

    override fun getShaderCircle(left: Float, top: Float, right: Float, bottom: Float): Shader {
        return BitmapShader(bitmapCircle, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR)
    }

    override fun getShaderPath(left: Float, top: Float, right: Float, bottom: Float): Shader {
        return BitmapShader(bitmapCircle, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR)
    }
}