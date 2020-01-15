package com.zfang.graphicdemo.activity.test

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.activity.matrix.*
import com.zfang.graphicdemo.base.BaseActivity
import com.zfang.graphicdemo.utils.getScreenHeight
import kotlinx.android.synthetic.main.activity_scroll_test.*

class ScrollTestActivity : BaseActivity() {

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, ScrollTestActivity::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_test)
        initToolbar(title = "滚动测试")
        setImageHeight(this)
    }

    private fun setImageHeight(ctx: Context) {
        findViewById<AppCompatImageView>(R.id.user_icon).post {
            val screenHeight = getScreenHeight(ctx)
            val childHeight = childContainer.height
            val parentFrameHeight = parent_frame.height
            Log.e("zfang", "screenHeight = $screenHeight, childHeight = $childHeight, parentFrameHeight = $parentFrameHeight")
            val userIconLP = user_icon.layoutParams
            userIconLP.height = parentFrameHeight
            childContainer.requestLayout()
        }
    }
}
