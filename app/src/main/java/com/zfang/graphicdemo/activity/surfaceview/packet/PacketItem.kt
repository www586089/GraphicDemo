package com.zfang.graphicdemo.activity.surfaceview.packet

import android.graphics.Bitmap


const val STATE_INVALIDATE = -1   //动画未开始
const val STATE_APPEAR = 0        //开始出现阶段[伴随alpha 0 -> 1]
const val STATE_MIDDLE = 1        //中间动画阶段
const val STATE_DISAPPEAR = 2     //消失阶段[伴随alpha 1 -> 0]
const val STATE_OVER_LIFE = 3     //当前item动画生命周期结束（此item不进行绘制）


const val TYPE_PACKET = 0        //红包类型
const val TYPE_RIAN = 1          //流星
const val TYPE_STAR = 2          //星星

/**
 * 下落过程中红包item
 */
data class PacketItem(
        var rainId: String = "",            //红包雨id
        var middleDuration: Long = 2000,     //当前红包持续时间
        var startX: Float = 0f,             //起点x坐标
        var startY: Float = 0f,             //起点y坐标
        var endX: Float = 0f,               //终点y坐标
        var endY: Float = 0f,               //终点y坐标
        var angle: Float = 0f,              //旋转角度（逆时针为正，顺时针为负）
        var rainType: Int = TYPE_PACKET,    //动画类型【红包、流星、星星】
) {
    val pointsArray = Array(4) {FloatArray(4)}
    var currentAlpha = 0f
    var alphaAppearDuration = 2600L
    var alphaDisappearDuration = 1600L
    var startTime: Long = 0
    var lastTime: Long = 0
    var currentX = 0f
    var currentY = 0f
    var state = STATE_INVALIDATE
    var lastFraction = 0f
    var outOffsetX = 0f

    lateinit var bitmap: Bitmap
    var bitmapWidth: Int = 0
    var bitmapHeight: Int = 0
    var started = false
}