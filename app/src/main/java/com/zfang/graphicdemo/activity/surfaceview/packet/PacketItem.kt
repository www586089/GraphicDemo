package com.zfang.graphicdemo.activity.surfaceview.packet

/**
 * 下落过程中红包item
 */
data class PacketItem(
        var rainId: String = "",    //红包雨id
        var duration: Int = 2000,   //当前红包持续时间
        var pX: Float = 0f,         //起点x坐标
        var pY: Float = 0f,         //起点y坐标
        var endX: Float = 0f,       //终点y坐标
        var endY: Float = 0f,       //终点y坐标
        var degree: Float = 0f,     //旋转角度（逆时针为正，顺时针为负）
) {
    var startTime: Long = 0
    var currentX = 0f
    var currentY = 0f
}