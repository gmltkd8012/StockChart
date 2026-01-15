package com.leecoder.data.source

import com.leecoder.stockchart.model.token.TokenData
import com.leecoder.stockchart.model.token.TokenError

interface AuthDataSource {

    suspend fun postToken(
        grantType: String,
        appsecret: String,
        appkey: String
    ): Result<TokenData>
}
