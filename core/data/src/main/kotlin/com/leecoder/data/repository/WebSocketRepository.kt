package com.leecoder.data.repository

import com.leecoder.stockchart.model.network.WebSocketState
import com.leecoder.stockchart.model.stock.StockTick
import com.leecoder.stockchart.model.token.TokenError
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import okhttp3.WebSocketListener

interface WebSocketRepository {

    val channelStockTick: ReceiveChannel<StockTick>
    val connectedWebSocketSession: Flow<WebSocketState>

    fun connect(url: String)
    fun disconnect()
    fun initSubscribe(symbols: List<String>)
    fun subscribe(symbol: String)
    fun unSubscribe(symbol: String)

    suspend fun postWebSocket(
        grantType: String,
        appkey: String,
        secretKey: String,
    ): Pair<Boolean, TokenError?>
}