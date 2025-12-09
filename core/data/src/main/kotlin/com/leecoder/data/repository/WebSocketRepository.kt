package com.leecoder.data.repository

import com.leecoder.stockchart.model.network.WebSocketState
import com.leecoder.stockchart.model.stock.NasdaqTick
import com.leecoder.stockchart.model.stock.StockTick
import com.leecoder.stockchart.model.token.TokenError
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import okhttp3.WebSocketListener

interface WebSocketRepository {

    val stockTick: SharedFlow<NasdaqTick>
    val connectedWebSocketSession: Flow<WebSocketState>

    fun connect()
    fun disconnect()
    suspend fun initSubscribe(symbols: List<String>)
    suspend fun subscribe(symbol: String)
    suspend fun unSubscribe(symbol: String)

    suspend fun postWebSocket(
        grantType: String,
        appkey: String,
        secretKey: String,
    ): Pair<Boolean, TokenError?>
}