package com.zfang.graphicdemo.activity.shader

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.base.BaseActivity

class ShaderTranslateActivity : BaseActivity() {

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, ShaderTranslateActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shader_translate)
        initToolbar(title = "常用操作(Translate)")
    }
}