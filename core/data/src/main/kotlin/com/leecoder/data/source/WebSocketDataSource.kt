package com.leecoder.data.source

import com.leecoder.stockchart.model.network.WebSocketState
import com.leecoder.stockchart.model.stock.StockTick
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import okhttp3.WebSocketListener

interface WebSocketDataSource {

    val channelStockTick: ReceiveChannel<StockTick>
    val connectedWebSocketSession: Flow<WebSocketState>

    fun connect(url: String)
    fun disconnect()
    fun sendMessage()
    fun initSubscribe(symbols: List<String>)
    fun subscribe(symbol: String)
    fun unSubscribe(symbol: String)
}