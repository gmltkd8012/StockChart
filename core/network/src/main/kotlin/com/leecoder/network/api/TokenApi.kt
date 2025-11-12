package com.leecoder.network.api

import com.leecoder.network.entity.TokenRequest
import com.leecoder.network.entity.TokenResponse
import com.leecoder.stockchart.appconfig.BuildConfig
import kotlinx.serialization.InternalSerializationApi
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface TokenApi {
    @POST(BuildConfig.KIS_AuthToken)
    suspend fun postToken(
        @Body body: TokenRequest
    ): Response<TokenResponse>
}