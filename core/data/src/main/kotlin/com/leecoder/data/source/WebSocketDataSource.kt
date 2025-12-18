package com.leecoder.data.source

import com.leecoder.stockchart.model.network.WebSocketState
import com.leecoder.stockchart.model.stock.NasdaqTick
import com.leecoder.stockchart.model.stock.StockTick
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import okhttp3.WebSocketListener

interface WebSocketDataSource {

    val channelStockTick: ReceiveChannel<NasdaqTick>
    val connectedWebSocketSession: Flow<WebSocketState>

    fun connect(url: String)
    fun disconnect()
    suspend fun initSubscribe(symbols: List<String>)
    suspend fun subscribe(symbol: String)
    suspend fun unSubscribe(symbol: String)

    /**
     * 트레이드 코드 변경 시 기존 구독을 해제하고 새 코드로 재구독
     */
    suspend fun refreshSubscriptionsWithNewTradeCode(oldCode: String, newCode: String)

    /**
     * 현재 구독 중인 심볼 목록 반환
     */
    fun getSubscribedSymbols(): Set<String>
}