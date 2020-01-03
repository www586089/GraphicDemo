package com.zfang.graphicdemo.test.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.Dispatcher

class Activity {
    private val mainScope = CoroutineScope(Dispatchers.Default)
    fun destroy() {
        mainScope.cancel()
    }

    fun doSomething() {
        mainScope.launch {
            repeat(10) {i ->
                launch {
                    delay((i + 1) * 200L)
                    println("Coroutine $i is done")
                }
            }
        }
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

fun main() = runBlocking<Unit> {
    val sum = (1..10).asFlow().filter {
        println("Filter $it")
        it % 2 == 0
    }.map {
        println("Map $it")
        "string $it"
    }.collect {
        println("Collect $it")
    }
    println(sum)
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