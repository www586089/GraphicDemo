package com.zfang.graphicdemo.view.test

/**
 *
 */
const val TO_ANCHOR_LEFT = 0
const val TO_ANCHOR_BOTTOM = 1
const val TO_ANCHOR_RIGHT = 2
const val TO_ANCHOR_TOP = 3
data class GuideInfo(var toAnchorDirection: Int, var scale: Float = 1f)
