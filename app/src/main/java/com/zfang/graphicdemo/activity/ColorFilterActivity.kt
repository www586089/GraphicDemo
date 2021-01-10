package com.zfang.graphicdemo.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.activity.cfilter.*
import com.zfang.graphicdemo.activity.matrix.*
import com.zfang.graphicdemo.base.BaseActivity

class ColorFilterActivity : BaseActivity() {

    private val TAG = "ColorFilterActivity"

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, ColorFilterActivity::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_filter)
        Log.d(TAG, "onCreate: ")
        initToolbar(title = "ColorFilter")
    }

    fun onClickSingleChannel(view: View) {
        SingleChannelColorFilterActivity.start(this)
    }

    //调整亮度
    fun onClickLuminance(view: View) {
        LuminanceColorFilterActivity.start(this)
    }

    fun onClickLuminance2(view: View) {
        LuminanceColorFilter2Activity.start(this)
    }

    //调整色调
    fun onClickHue(view: View) {
        HueColorFilterActivity.start(this)
    }

    fun onClickHue2(view: View) {
        HueColorFilter2Activity.start(this)
    }

    //调整颜色饱和度
    fun onClickSaturation(view: View) {
        SaturationFilterActivity.start(this)
    }

    //光线过滤
    fun onClickLightColorFilter(view: View) {
        LightColorFilterActivity.start(this)
    }

    //颜色混合
    fun onClickPorterDuff(view: View) {
        PorterDuffColorFilterActivity.start(this)
    }

    fun onClickPorterDuff2(view: View) {
        PorterDuffColorFilter2Activity.start(this)
    }
}
