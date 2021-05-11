package com.zfang.graphicdemo.activity.surfaceview.packet

import android.graphics.*
import android.util.Log
import kotlin.random.Random

const val stepMin = 4f
const val stepMax = 8f
class Packet(var startX: Float, var endX: Float, var startY: Float, endY: Float, var angle: Float,
             var bitmap: Bitmap, val paint: Paint, val random: Random, val matrix: Matrix, val startRegion: RectF, val endRegion: RectF) {
    var currentX = startX
    var currentY = startY
    val PI = (Math.PI * (angle) / 180.0)
    var scale = 1 + random.nextFloat() * 0.5f
    var bitmapWidth = bitmap.width.toFloat() * scale
    var bitmapHeight = bitmap.height.toFloat() * scale
    var appearDuration = 0L
    var middleDuration = 0L
    var disappearDuration = 0L
    var step = random.nextFloat() * (stepMax - stepMin) + stepMin
    var stepAppear = 0f
    var stepMiddle = 0f
    var stepDisappear = 0f
    var stepAlphaAppear = 0f
    var stepAlphaDisappear = 0f
    var alpha = 0f


    fun config(fps: Long) {
        stepAppear = startRegion.height() / (appearDuration / fps)
        stepMiddle = (endRegion.top - startRegion.bottom) / (middleDuration / fps)
        stepDisappear = endRegion.height() / (disappearDuration / fps)

        stepAlphaAppear = 1.0f / (appearDuration / fps)
        stepAlphaDisappear = 1.0f / (disappearDuration / fps)
        Log.d("zfang", "stepAppear = $stepAppear, stepMiddle = $stepMiddle, " +
                "stepDisappear = $stepDisappear, stepAlphaAppear = ${stepAlphaAppear}, stepAlphaDisappear = ${stepAlphaDisappear}")
    }

    private fun move(canvasWidth: Float, canvasHeight: Float) {
        if (currentY < startRegion.height()) {
            step = stepAppear
            alpha += stepAlphaAppear
        } else if (currentY < endRegion.top) {
            alpha = 1f
            step = stepMiddle
        } else if (currentY < endRegion.bottom){
            step = stepDisappear
            alpha -= stepAlphaDisappear
        }
        currentY += step
        currentX -= (step / Math.tan(PI)).toFloat()

        if (currentY > endRegion.bottom || currentX <= - (scale * bitmapWidth) / 2) {//reset
            currentX = canvasWidth / 3 + random.nextFloat() * (canvasWidth * 4 / 3)
            currentY = 0f
            alpha = 0f
        }
    }

    fun draw(canvas: Canvas, canvasWidth: Float, canvasHeight: Float) {
        move(canvasWidth, canvasHeight)
        canvas.drawRect(startRegion, paint)
        canvas.drawRect(endRegion, paint)
        matrix.reset()
        matrix.setScale(scale, scale)
        matrix.postTranslate(currentX - bitmapWidth / 2, currentY - bitmapHeight / 2)
        matrix.postRotate(90 - angle, currentX, currentY)
        paint.alpha = (alpha * 0xff).toInt()
        canvas.drawBitmap(bitmap, matrix, paint)
//        canvas.drawBitmap(bitmap, currentX - bitmapWidth / 2, currentY - bitmapHeight / 2, paint)
    }
}