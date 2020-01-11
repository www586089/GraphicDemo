package com.zfang.graphicdemo.activity.view.drag

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import com.zfang.graphicdemo.R
import com.zfang.graphicdemo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_view_rotation.*

class ViewDragActivity2 : BaseActivity() {

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, ViewDragActivity2::class.java))
        }

        private const val TAG = "ViewDragActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_drag2)
        initToolbar(title = "ViewDrag(ViewDragHelper)")
    }
}
