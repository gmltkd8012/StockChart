package com.leecoder.data.repository

import android.util.Log
import com.leecoder.data.source.WebSocketDataSource
import com.leecoder.network.api.WebSocketApi
import com.leecoder.network.entity.WebSocketRequest
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.model.network.WebSocketState
import com.leecoder.stockchart.model.stock.StockTick
import com.leecoder.stockchart.model.token.TokenError
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketRepositoryImpl @Inject constructor(
    private val webSocketApi: WebSocketApi,
    private val webSocketDataSource: WebSocketDataSource,
    private val dataStoreRepository: DataStoreRepository,
): WebSocketRepository {

    override val channelStockTick: ReceiveChannel<StockTick>
        get() = webSocketDataSource.channelStockTick

    override val connectedWebSocketSession: Flow<WebSocketState>
        get() = webSocketDataSource.connectedWebSocketSession

    override fun connect(url: String) {
        webSocketDataSource.connect(url)
    }

    override fun disconnect() {
        webSocketDataSource.disconnect()
    }

    override fun sendMessage() {
        webSocketDataSource.sendMessage()
    }

    override fun initSubscribe(symbols: List<String>) {
        webSocketDataSource.initSubscribe(symbols)
    }

    override fun subscribe(symbol: String) {
       webSocketDataSource.subscribe(symbol)
    }

    override fun unSubscribe(symbol: String) {
        webSocketDataSource.unSubscribe(symbol)
    }

    override suspend fun postWebSocket(
        grantType: String,
        appkey: String,
        secretKey: String
    ): Pair<Boolean, TokenError?> {
        val response = webSocketApi.postWebSocket(WebSocketRequest(grantType, appkey, secretKey))

        return try {
            if (response.isSuccessful) {
                val body = response.body() ?: return false to null
                dataStoreRepository.refreshKrInvestmentWebSocket(body.approvalKey)
                Log.d("heesang", "postWebSocket: ${body.approvalKey}")
                true to null
            } else {
                throw Exception(response.errorBody()?.string())
            }
        } catch (e: Exception) {
            val errorBody = e.message?.let {
                Json.decodeFromString<TokenError>(it)
            } ?: TokenError()

            false to TokenError(errorBody.errorDescription, errorBody.errorCode)
        }
    }
}