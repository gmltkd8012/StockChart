package com.leecoder.data.source

import com.leecoder.stockchart.model.stock.StockTick
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import okhttp3.WebSocketListener

interface WebSocketDataSource {

    val channelStockTick: ReceiveChannel<StockTick>

    fun connect(url: String)
    fun disconnect()
    fun sendMessage()
    fun addSubscribe(iscd: String)
}