package com.zfang.graphicdemo.test.coroutine

typealias CloseLis = () -> Unit

/**
 * dialog 队列 接口
 */
interface IQueueDialog {

    /**
     * 弹框展示自身
     */
    fun show()

    /**
     * 弹窗消失回调
     */
    var dismissLis: CloseLis

}