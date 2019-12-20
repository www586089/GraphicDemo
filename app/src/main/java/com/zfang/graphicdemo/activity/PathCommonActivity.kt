package com.zfang.graphicdemo.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.activity.matrix.*
import com.zfang.graphicdemo.activity.path.LineCapActivity
import com.zfang.graphicdemo.base.BaseActivity

class PathCommonActivity : BaseActivity() {

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, PathCommonActivity::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_path_common)
        initToolbar(title = "路径相关操作")
    }

    fun onLineCapClick(view: View) {
        LineCapActivity.start(this)
    }
}
