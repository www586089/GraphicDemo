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
import com.zfang.graphicdemo.view.test.GuideInfoHelper
import com.zfang.graphicdemo.view.test.TO_ANCHOR_BOTTOM
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
            val guideViewContent1 = LayoutInflater.from(this).inflate(R.layout.home_guide_layout_1, null)
            guideViewContent1.findViewById<AppCompatTextView>(R.id.guide_home_next).setOnClickListener {
                guideView.showNext()
            }
            val guideViewContent2 = LayoutInflater.from(this).inflate(R.layout.home_guide_layout_2, null)
            guideViewContent2.findViewById<AppCompatTextView>(R.id.guide_home_know).setOnClickListener {
                guideView.completeGuide()
            }
            val guideHomeNext = guideViewContent1.findViewById<AppCompatTextView>(R.id.guide_home_next)
            val guideHomeComplete = guideViewContent2.findViewById<AppCompatTextView>(R.id.guide_home_know)
            GuideInfoHelper.addGuideInfo(GuideInfo(anchorView = path_effect, clickView = guideHomeNext,
                guideViewContent = guideViewContent1, toAnchorDirection = TO_ANCHOR_LEFT, scale = 0.235f, cornerRadius = 5.px2Dp(this).toFloat()))
            GuideInfoHelper.addGuideInfo(GuideInfo(anchorView = image_test, clickView = guideHomeComplete,
                guideViewContent = guideViewContent2, toAnchorDirection = TO_ANCHOR_BOTTOM, scale = 0.38f, cornerRadius = 5.px2Dp(this).toFloat()))

            guideView.showGuide(GuideInfoHelper)
        }
    }

    fun onClickScrollTest(view: View) {
        ScrollTestActivity.start(this)
    }

    fun showNextGuide(view: View) {
        val guideViewContent1 = LayoutInflater.from(this).inflate(R.layout.mine_guide_layout_1, null)
        guideViewContent1.findViewById<AppCompatTextView>(R.id.guide_mine_next).setOnClickListener {
            guideView.showNext()
        }
        val guideViewContent2 = LayoutInflater.from(this).inflate(R.layout.mine_guide_layout_2, null)
        guideViewContent2.findViewById<AppCompatTextView>(R.id.guide_mine_know).setOnClickListener {
            guideView.completeGuide()
        }
        val guideHomeNext = guideViewContent1.findViewById<AppCompatTextView>(R.id.guide_mine_next)
        val guideHomeComplete = guideViewContent2.findViewById<AppCompatTextView>(R.id.guide_mine_know)
        GuideInfoHelper.addGuideInfo(GuideInfo(anchorView = myEarn, clickView = guideHomeNext,
            guideViewContent = guideViewContent1, toAnchorDirection = TO_ANCHOR_BOTTOM, scale = 0.016f, cornerRadius = 0.px2Dp(this).toFloat()))
        GuideInfoHelper.addGuideInfo(GuideInfo(anchorView = taskCenter, clickView = guideHomeComplete,
            guideViewContent = guideViewContent2, toAnchorDirection = TO_ANCHOR_BOTTOM, scale = 0.28f, cornerRadius = 18.px2Dp(this).toFloat(),gravity = 0.6f))

        guideView.showGuide(GuideInfoHelper)
    }
}
