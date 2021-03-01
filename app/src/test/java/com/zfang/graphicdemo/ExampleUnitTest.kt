package com.zfang.graphicdemo

import android.text.TextUtils
import android.util.Log
import org.junit.Test

import org.junit.Assert.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

        var countDown = 9999
        val tv = transform(countDown, "%s后结束")
        val subscription = Observable.interval(1, TimeUnit.SECONDS)
            .takeUntil { countDown <= 0 }
            .map { transform(--countDown, "%s后结束") }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ text -> Log.e("zfang", text) }, { throwable -> Log.d("zfang", "something error, msg = ${throwable?.message}")})
    }

    private fun transform(number: Int, desc: String): String {
        if (number <= 0) {
            return "活动已结束"
        }
        val hours = number / 3600
        val minitues = (number - hours * 3600) / 60
        val seconds = number - hours * 3600 - minitues * 60
        val timeStr = String.format("%02d:%02d:%02d", hours, minitues, seconds)
        if (!TextUtils.isEmpty(desc) && desc.contains("%s")) {
            return String.format(desc, timeStr)
        } else {
            return timeStr + "后结束"
        }
    }
}
