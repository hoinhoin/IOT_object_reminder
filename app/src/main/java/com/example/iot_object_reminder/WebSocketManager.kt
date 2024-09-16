package com.example.iot_object_reminder

import android.content.Context
import android.widget.Toast
import okhttp3.*
import java.util.concurrent.TimeUnit

class WebSocketManager(private val context: Context, private val listener: WebSocketListener) {
    private lateinit var webSocket: WebSocket

    fun initWebSocket(url: String) {
        val client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()

        val request = Request.Builder()
            .url(url) // 웹소켓 서버 주소
            .build()

        webSocket = client.newWebSocket(request, listener)
    }

    fun sendMessage(message: String) {
        webSocket.send(message)
    }

    fun closeConnection() {
        webSocket.close(1000, "앱 종료")
    }
}
