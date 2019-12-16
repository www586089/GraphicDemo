package com.zfang.graphicdemo.activity.cfilter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.activity.matrix.*
import com.zfang.graphicdemo.base.BaseActivity

class SingleChannelColorFilterActivity : BaseActivity() {

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SingleChannelColorFilterActivity::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_filter_channel)
        initToolbar(title = "ColorFilter-通道变换")
    }
}
