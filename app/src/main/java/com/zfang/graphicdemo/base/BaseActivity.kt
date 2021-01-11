package com.zfang.graphicdemo.base

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.zfang.graphicdemo.R

open class BaseActivity() : AppCompatActivity() {

    private val TAG = "BaseActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")//add a log in onCreate
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Log.e(TAG, "onOptionsItemSelected: this is good")
        when (item!!.itemId) {
            android.R.id.home -> finish()
            else -> {
                Log.e(TAG, "nothing todo 22")
            }
        }
        Log.e(TAG, "onOptionsItemSelected: I'm Coming")
        return super.onOptionsItemSelected(item)
    }

    protected fun initToolbar(displayHomeAsUp: Boolean = true, title: String = getString(R.string.app_name)) {
        Log.e(TAG, "initToolbar: fix a bug")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle(title)
    }
}