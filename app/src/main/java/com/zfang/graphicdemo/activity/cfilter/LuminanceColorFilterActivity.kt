package com.zfang.graphicdemo.activity.cfilter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.base.BaseActivity

class LuminanceColorFilterActivity : BaseActivity() {

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, LuminanceColorFilterActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_luminance_color_filter)
        initToolbar(title = "Luminance(颜色亮度)")
    }
}