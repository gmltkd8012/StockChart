package com.leecoder.data.repository

import com.leecoder.stockchart.model.stock.StockTick
import com.leecoder.stockchart.model.token.TokenError
import kotlinx.coroutines.channels.ReceiveChannel
import okhttp3.WebSocketListener

interface WebSocketRepository {

    val channelStockTick: ReceiveChannel<StockTick>

    fun connect(url: String)
    fun disconnect()
    fun sendMessage()

    suspend fun postWebSocket(
        grantType: String,
        appkey: String,
        secretKey: String,
    ): Pair<Boolean, TokenError?>
}