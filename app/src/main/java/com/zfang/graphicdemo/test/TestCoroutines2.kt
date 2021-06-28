package com.example.kotlintest.test

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: WeiTao
 * @date: 2021/6/21
 */
fun main() {
    TestCoroutines2().test()
}

class TestCoroutines2 {

    val mutex = Mutex()
    val msgBoxes = intArrayOf(0, 0, 0, 0, 0)
    val checkers: Array<AtomicBoolean> = arrayOf(
        AtomicBoolean(false),
        AtomicBoolean(false),
        AtomicBoolean(false),
        AtomicBoolean(false),
        AtomicBoolean(false)
    )

    fun test() {
        for (j in 0..10) {
            GlobalScope.launch {
                for (i in msgBoxes.indices) {
                    GlobalScope.launch {
                        delay(20L)
                        println("i = $i, thread = ${Thread.currentThread().hashCode()}")
                        if/*while*/ (msgBoxes[i] == 0) {
                            delay(20L)
                            add(i)
                        }
                        println("after call create: ${msgBoxes[i]}")
                    }
                }
            }
        }
        Thread.sleep(10000L)

        msgBoxes.forEach {
            println(it)
        }
    }

    private suspend fun add(i: Int) {
//        mutex.withLock {
        println("do create, i = $i, called = ${checkers[i].get()}")
        if (checkers[i].compareAndSet(false, true)) {
            if (msgBoxes[i] == 0) {
                println("before create, i = $i")
                delay(20L)
                println("after create, i = $i")
                msgBoxes[i] = msgBoxes[i] + 1
            }
        }
        while (true) {

            if (msgBoxes[i] == 1) {
                println("break, i = $i")
                break
            }
        }
    }
}