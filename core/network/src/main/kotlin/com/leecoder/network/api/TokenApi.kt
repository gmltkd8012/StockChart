package com.leecoder.network.api

import com.leecoder.network.entity.TokenEntity
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface TokenApi {

    @POST("/oauth2/tokenP")
    suspend fun postToken(
        @Query("grant_type") grantType: String,
        @Query("appsecret") appsecret: String,
        @Query("appkey") appkey: String,
    ): Response<TokenEntity>
}