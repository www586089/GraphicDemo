package com.zfang.graphicdemo

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


fun main() {
    testGet()
}


fun testGet() {
    val url = "http://cf.colorbook.info/m/config?pubid=100194&moduleid=7070&pkg_name=com.colorbook.coloring&pkg_ver=1&file_ver=20171111"
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .build()
    val call = client.newCall(request)
    try {
        val response = call.execute()
        System.out.println(response.body()?.string())
    } catch (e: IOException) {
        e.printStackTrace()
    }

}