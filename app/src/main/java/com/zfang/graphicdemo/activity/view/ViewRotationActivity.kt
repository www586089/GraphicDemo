package com.zfang.graphicdemo.activity.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_view_rotation.*

class ViewRotationActivity : BaseActivity() {

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, ViewRotationActivity::class.java))
        }

        private const val TAG = "ViewRotationActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_rotation)
        initToolbar(title = "View旋转")
        init()
    }

    private fun init() {
        seek_bar.max = 100
        seek_bar.min = 0
        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val rotation = (progress - 50).toFloat()
                    Log.e(TAG, "rotation = $rotation")
                    frame.rotation = rotation
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }
}
