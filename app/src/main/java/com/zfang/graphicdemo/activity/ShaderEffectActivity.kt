package com.zfang.graphicdemo.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.activity.matrix.*
import com.zfang.graphicdemo.activity.path.LineCapActivity
import com.zfang.graphicdemo.activity.path.LineJoinActivity
import com.zfang.graphicdemo.activity.shader.BitmapShaderActivity
import com.zfang.graphicdemo.activity.shader.LinearGradientShaderActivity
import com.zfang.graphicdemo.activity.shader.RadialGradientShaderActivity
import com.zfang.graphicdemo.activity.shader.SweepGradientShaderActivity
import com.zfang.graphicdemo.base.BaseActivity

class ShaderEffectActivity : BaseActivity() {

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, ShaderEffectActivity::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shader_effect)
        initToolbar(title = "Shader相关操作")
    }

    fun onLinearClick(view: View) {
        LinearGradientShaderActivity.start(this)
    }

    fun onLineJoinClick(view: View) {
        RadialGradientShaderActivity.start(this)
    }

    fun onClickSweepGradient(view: View) {
        SweepGradientShaderActivity.start(this)
    }

    fun onClickBitmapShader(view: View) {
        BitmapShaderActivity.start(this)
    }
}
