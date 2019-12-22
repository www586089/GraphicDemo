package com.zfang.graphicdemo.activity.shader

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.base.BaseActivity

class ShaderScaleActivity : BaseActivity() {

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, ShaderScaleActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shader_scale)
        initToolbar(title = "常用操作(Scale)")
    }
}