package com.leecoder.data.token

import com.leecoder.network.util.NetworkResult
import com.leecoder.stockchart.model.token.TokenData
import com.leecoder.stockchart.model.token.TokenError
import kotlinx.coroutines.flow.Flow

interface TokenRepository {

    suspend fun postToken(
        grantType: String,
        appsecret: String,
        appkey: String,
    ): Pair<Boolean, TokenError?>
}