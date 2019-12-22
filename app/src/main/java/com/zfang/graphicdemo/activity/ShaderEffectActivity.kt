package com.zfang.graphicdemo.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.activity.shader.*
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

    fun onClickComposeShader(view: View) {
        ComposeShaderActivity.start(this)
    }

    fun onClickShowComposeShader(view: View) {
        ShowComposeShaderActivity.start(this)
    }

    fun onClickShaderTranslate(view: View) {
        ShaderTranslateActivity.start(this)
    }

    fun onClickShaderScale(view: View) {
        ShaderScaleRectActivity.start(this)
    }

    fun onClickShaderScaleCircle(view: View) {
        ShaderScaleCircleActivity.start(this)
    }
}
