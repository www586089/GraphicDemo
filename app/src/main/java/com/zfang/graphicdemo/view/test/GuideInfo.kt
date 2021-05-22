package com.zfang.graphicdemo.view.test

import android.view.View

/**
 *
 */
const val TO_ANCHOR_LEFT = 0
const val TO_ANCHOR_BOTTOM = 1
const val TO_ANCHOR_RIGHT = 2
const val TO_ANCHOR_TOP = 3
data class GuideInfo(val anchorView: View, val clickView: View, val guideViewContent: View,
                     var toAnchorDirection: Int, var scale: Float = 1f, var cornerRadius: Float = 1f,
                     var gravity: Float = 0.5f)
