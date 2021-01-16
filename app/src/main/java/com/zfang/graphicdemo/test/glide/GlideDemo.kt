package com.zfang.graphicdemo.test.glide

import okhttp3.OkHttpClient
import okhttp3.Request
import rx.*
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.functions.Func1
import rx.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.function.Consumer


fun main() {
    testRxJava()
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