package com.zfang.graphicdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zfang.graphicdemo.activity.*
import com.zfang.graphicdemo.activity.matrix.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickMatrix(view: View) {
        MatrixActivity.start(this)
    }

    fun onClickPathEffect(view: View) {
        PathEffectActivity.start(this)
    }

    fun onClickPathCommon(view: View) {
        PathCommonActivity.start(this)
    }

    fun onClickShader(view: View) {
        ShaderEffectActivity.start(this)
    }

    fun onClickRotate(view: View) {
        RotateActivity.start(this)
    }

    fun onClickColorFilter(view: View) {
        ColorFilterActivity.start(this)
    }

    fun onClickSkew(view: View) {
        SkewActivity.start(this)
    }

    fun onClickClip(view: View) {
        ClipActivity.start(this)
    }

    fun onClickInvert(view: View) {
        InvertActivity.start(this)
    }

    fun onClickMap(view: View) {
        MapActivity.start(this)
    }

    fun onClickTest(view: View) {
        TestActivity.start(this)
    }
}
