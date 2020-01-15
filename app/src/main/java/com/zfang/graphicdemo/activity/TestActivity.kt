package com.zfang.graphicdemo.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.activity.matrix.*
import com.zfang.graphicdemo.activity.test.ScrollTestActivity
import com.zfang.graphicdemo.base.BaseActivity

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
    }

    fun onClickScrollTest(view: View) {
        ScrollTestActivity.start(this)
    }
}
