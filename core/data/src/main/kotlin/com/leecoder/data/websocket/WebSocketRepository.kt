package com.leecoder.data.websocket

import com.leecoder.stockchart.model.token.TokenError

interface WebSocketRepository {

    suspend fun postWebSocket(
      grantType: String,
      appkey: String,
      secretKey: String,
    ): Pair<Boolean, TokenError?>
}