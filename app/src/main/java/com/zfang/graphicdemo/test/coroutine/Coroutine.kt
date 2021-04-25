package com.zfang.graphicdemo.test.coroutine

import android.util.Log
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import rx.Observable
import rx.Subscriber
import rx.functions.Action1
import rx.plugins.RxJavaHooks
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.Comparator
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.math.max

typealias CreateDialogAction = (topActivity: FragmentActivity, coroutineScope: CoroutineScope) -> Deferred<IQueueDialog?>

data class QueueDialogBean(val priority: Int, val createDialogAction: (topActivity: FragmentActivity, coroutineScope: CoroutineScope) -> Deferred<IQueueDialog?>?)
class Activity {
    private val mainScope = CoroutineScope(Dispatchers.Default)
    fun destroy() {
        mainScope.cancel()
    }

    fun doSomething() {
        mainScope.launch {
            repeat(10) { i ->
                launch {
                    delay((i + 1) * 200L)
                    println("Coroutine $i is done")
                }
            }
        }
    }
}

fun getToken(): Observable<String?>? {
    println("zfang")
    val observable = Observable.create { subscriber: Subscriber<in String?> ->
        WatchMan.getToken(object : GetTokenCallback {
            override fun onResult(code: Int, str: String, str2: String) {
                println("onResult")
                subscriber.onNext("1")
                subscriber.onCompleted()
            }
        })
    }
    return observable.doOnSubscribe { println("zfang start collect") }
        .doOnCompleted { println("zfang collect end") }
        .doOnError { println("on error1111 ") }
        .timeout(3000, TimeUnit.MILLISECONDS)
        .doOnError { println("on error2222 ") }.onErrorReturn {
            "default"
        }

}
val threadLocal = ThreadLocal<String?>()

fun foo():Flow<Int> = flow {
    for (i in 1..3) {
        delay(100)
        println("Emitting $i")
        emit(i)
    }
}

suspend fun performRequest(request: Int):String {
    delay(1000)
    return "response $request"
}

fun numbers(): Flow<Int> = flow {
    try {
        emit(1)
        emit(2)
        println("This line will not executed")
        emit(3)
    } finally {
        println("Finally in numbers")
    }
}
private val timer by lazy { Timer() }
private var timing = 0
private fun startTimer() {
    timer.scheduleAtFixedRate(1000, 1000) {
        timing++
//        val time = {//1v1连麦计时
            val hour = timing / 3600
            val minute = (timing / 60) % 60
            val second = timing % 60
//            Triple(hour, minute, second)
//        }
        println(String.format("%02d:%02d:%02d", hour, minute, second))
    }
}
fun main() = runBlocking<Unit> {
    startTimer()
    delay(2)
    startTimer()
//    stackTest()
//    queueTest()
//    RxJavaHooks.setOnError {
//        println("Hook error = ${it}")
//    }
//    getToken()?.subscribe(Action1 { token ->
//        println("token = $token")
//    }, {
//        println("on errr = ${it}")
//    })
//    println("result = ${Test.result?.a ?: "dddd"}")
}

fun showNext() {
    if (dialogQueue.isEmpty()) {
        println("done")
    } else {
        val bean = dialogQueue.poll()
        println("priority = ${bean.priority}")
        showNext()
    }
}

fun queueTest() {
    dialogQueue.add(QueueDialogBean(8) { topActivity, coroutineScope -> null })
    dialogQueue.add(QueueDialogBean(4) { topActivity, coroutineScope -> null })
    dialogQueue.add(QueueDialogBean(3) { topActivity, coroutineScope -> null })
    dialogQueue.add(QueueDialogBean(2) { topActivity, coroutineScope -> null })
    dialogQueue.add(QueueDialogBean(1) { topActivity, coroutineScope -> null })
    showNext()
}
private val DIALOG_QUEUE_COMPARATOR = Comparator<QueueDialogBean> { bean1, bean2 ->
    val p1 = bean1?.priority ?: 0
    val p2 = bean2?.priority ?: 0
    p1 - p2
}

private val dialogQueue = PriorityQueue(11, DIALOG_QUEUE_COMPARATOR)

fun stackTest() = runBlocking {
    val dispatcher = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()).asCoroutineDispatcher();
    repeat(100_00_00_0){
        launch(dispatcher) {
            delay(30000)
            println("Running = $it")
        }
    }
    dispatcher.close()
}

suspend fun failedConcurrentSum(): Int = coroutineScope {
    val one = async<Int> {
        try {
            delay(Long.MAX_VALUE)
            42
        } finally {
            println("First child was cancelled")
        }
    }

    val two = async<Int> {
        println("Second child throws an exception")
        throw ArithmeticException()
    }
    one.await() + two.await()
}

suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }
    one.await() + two.await()
}

fun somethingUsefulOneAsync() = GlobalScope.async {
    doSomethingUsefulOne()
}

fun somethingUsefulTwoAsync() = GlobalScope.async {
    doSomethingUsefulTwo()
}

suspend fun doSomethingUsefulOne(): Int {
    delay(1000)
    println("still run")
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000)
    return 29
}

private suspend fun doWorld() {
    delay(200L)
    println("task from runBlocking!")
}

class WatchMan {
    companion object {
        fun getToken(callback: GetTokenCallback) {
            Thread.sleep(4000)
            callback.onResult(1, "str", "title")
        }
    }
}

interface GetTokenCallback {
    fun onResult(code: Int, str: String, str2: String)
}