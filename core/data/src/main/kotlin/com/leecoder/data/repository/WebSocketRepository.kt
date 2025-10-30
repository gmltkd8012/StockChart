package com.leecoder.data.repository

import com.leecoder.stockchart.model.stock.StockTick
import com.leecoder.stockchart.model.token.TokenError
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import okhttp3.WebSocketListener

interface WebSocketRepository {

    val channelStockTick: ReceiveChannel<StockTick>
    val connectedWebSocketSession: Flow<Boolean>

    fun connect(url: String)
    fun disconnect()
    fun sendMessage()
    fun initSubscribe(symbols: List<String>)
    fun addSubscribe(symbol: String)

    suspend fun postWebSocket(
        grantType: String,
        appkey: String,
        secretKey: String,
    ): Pair<Boolean, TokenError?>
}