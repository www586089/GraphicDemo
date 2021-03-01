package com.zfang.graphicdemo.test.glide

import android.text.TextUtils
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import rx.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.functions.Func1
import rx.schedulers.Schedulers
import java.io.IOException
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit
import java.util.function.Consumer


fun main() {
    testException()
}

fun testException() {
    var countDown = 15
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
        throw RuntimeException("no no no")
//        return "活动已结束"
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
fun testRxJava(): Unit {
    Observable.create(object : Observable.OnSubscribe<Int> {
        override fun call(t: Subscriber<in Int>?) {
            println("zfang, subcribe, thread = ${Thread.currentThread().name}")
            t?.onNext(1)
            t?.onCompleted()
        }
    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnNext(object : Action1<Int> {
        override fun call(t: Int?) {
            println("zfang, doOnNext, t = $t, thread = thread${Thread.currentThread().name}")
        }
    }).observeOn(Schedulers.computation()).doOnNext { t -> println("zfang2, doOnNext, t = $t, thread = thread${Thread.currentThread().name}") }
        .map { t -> "$t from map" }.flatMap { t ->
        Observable.just("$t + 1", "$t + 2", "$t + 3", "$t + 4").delay(2, TimeUnit.SECONDS, Schedulers.io())
    }.observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<String> {
        override fun onCompleted() {
            println("zfang, onCompleted")
        }

        override fun onError(e: Throwable?) {
            println("zfang, onError")
        }

        override fun onNext(t: String?) {
            println("zfang, onNext, thread = ${Thread.currentThread().name}, result = $t")
        }
    })
}

fun testGet() {
    val url = "http://cf.colorbook.info/p/config?pubid=100194&moduleid=7070&pkg_name=com.colorbook.coloring&pkg_ver=1&file_ver=20171111"
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .build()
    val call = client.newCall(request)
    try {
        val response = call.execute()
        System.out.println(response.body!!.string())
    } catch (e: IOException) {
        e.printStackTrace()
    }

}