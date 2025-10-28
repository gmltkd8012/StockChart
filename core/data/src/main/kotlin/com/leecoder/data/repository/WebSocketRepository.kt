package com.leecoder.data.repository

import com.leecoder.stockchart.model.token.TokenError
import okhttp3.WebSocketListener

interface WebSocketRepository {
    fun connect(url: String)
    fun disconnect()
    fun sendMessage()

    suspend fun postWebSocket(
        grantType: String,
        appkey: String,
        secretKey: String,
    ): Pair<Boolean, TokenError?>
}