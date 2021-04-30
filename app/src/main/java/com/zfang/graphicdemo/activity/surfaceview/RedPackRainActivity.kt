package com.zfang.graphicdemo.activity.surfaceview

import android.content.Context
import android.content.Intent
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.View
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.activity.surfaceview.packet.PacketManager
import com.zfang.graphicdemo.base.BaseActivity
import com.zfang.graphicdemo.utils.getScreenHeight
import com.zfang.graphicdemo.utils.getScreenWidth
import kotlinx.android.synthetic.main.activity_red_pack_rain.*

class RedPackRainActivity: BaseActivity() {
    companion object {
        private val TAG = "RedPackRainActivity"
        fun startActivity(ctx: Context) {
            ctx.startActivity(Intent(ctx, RedPackRainActivity::class.java))
        }
    }

    private var surfaceLocation: IntArray = IntArray(2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_red_pack_rain)
        initToolbar(title = "红包雨")
        packetView.post {
            packetView.getLocationOnScreen(surfaceLocation)
            Log.d(TAG, "width = ${packetView.width}, height = ${packetView.height}, left = ${surfaceLocation[0]}, top = ${surfaceLocation[1]}")
        }
    }

    fun startRedPacketRain(view: View) {
        val width = getScreenWidth(this).toFloat()
        val height = getScreenHeight(this).toFloat()
        val startRegion = RectF(0f, 0f, width, packetView.height.toFloat() / 3)
        val endRegion = RectF(0f, 0.75f * packetView.height.toFloat(), width, packetView.height.toFloat())
        PacketManager.generatePacket("12", startRegion, endRegion, packetCount = 5)
        PacketManager.startRainAnimation()
    }
}