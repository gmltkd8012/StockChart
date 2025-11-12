package com.leecoder.network.api

import com.leecoder.network.entity.WebSocketEntity
import com.leecoder.network.entity.WebSocketRequest
import com.leecoder.stockchart.appconfig.BuildConfig
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface WebSocketApi {
    @POST(BuildConfig.KIS_WebSocket_ApprovalKey)
    suspend fun postWebSocket(
        @Body body: WebSocketRequest
    ): Response<WebSocketEntity>
}