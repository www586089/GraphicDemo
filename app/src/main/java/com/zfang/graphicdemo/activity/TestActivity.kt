package com.zfang.graphicdemo.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.activity.test.ScrollTestActivity
import com.zfang.graphicdemo.base.BaseActivity
import com.zfang.graphicdemo.common.px2Dp
import com.zfang.graphicdemo.view.test.GuideInfo
import com.zfang.graphicdemo.view.test.TO_ANCHOR_LEFT
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : BaseActivity() {

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, TestActivity::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        initToolbar(title = "实验")
        supportActionBar?.hide()
        guideView.post {
            val guideViewContent = LayoutInflater.from(this).inflate(R.layout.home_guide_layout_1, null)
            guideViewContent.findViewById<AppCompatTextView>(R.id.guide_home_next).setOnClickListener {
                guideView.showNext(image_test)
            }
            guideView.setDisplayHollowGuidView(path_effect, guideViewContent, GuideInfo(toAnchorDirection = TO_ANCHOR_LEFT, scale = 0.235f))
        }
    }

    fun onClickScrollTest(view: View) {
        ScrollTestActivity.start(this)
    }
}
