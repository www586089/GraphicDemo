package com.zfang.graphicdemo.activity.surfaceview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.base.BaseActivity

class RedPackRainActivity: BaseActivity() {
    companion object {
        fun startActivity(ctx: Context) {
            ctx.startActivity(Intent(ctx, RedPackRainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_red_pack_rain)
        initToolbar(title = "红包雨")
    }
}