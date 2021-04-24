package com.zfang.graphicdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.activity.surfaceview.RedPackRainActivity
import com.zfang.graphicdemo.base.BaseActivity

class SurfaceViewActivity: BaseActivity() {

    companion object {
        fun startActivity(ctx: Context) {
            ctx.startActivity(Intent(ctx, SurfaceViewActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surface_view)
        initToolbar(title = "SurfaceView应用")
    }

    fun onClickBasicUse(view: View) {}
    fun onClickRedRain(view: View) {
        RedPackRainActivity.startActivity(this)
    }
}