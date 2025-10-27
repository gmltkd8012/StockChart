package com.leecoder.data.websocket

import android.util.Log
import com.leecoder.network.api.WebSocketApi
import com.leecoder.network.entity.WebSocketRequest
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.model.token.TokenError
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketRepositoryImpl @Inject constructor(
    private val webSocketApi: WebSocketApi,
    private val dataStoreRepository: DataStoreRepository,
): WebSocketRepository {

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