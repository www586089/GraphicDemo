package com.zfang.graphicdemo.activity.shader

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.base.BaseActivity

class ComposeShaderActivity : BaseActivity() {

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, ComposeShaderActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose_shader)
        initToolbar(title = "混合渲染(ComposeShader)")
    }
}