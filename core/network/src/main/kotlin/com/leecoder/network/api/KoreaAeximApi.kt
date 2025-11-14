package com.leecoder.network.api

import com.leecoder.network.entity.ExchangeInfoEntity
import com.leecoder.stockchart.appconfig.BuildConfig
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface KoreaAeximApi {

    @GET(BuildConfig.KOREA_AEXIM_EXCHANGE_ENTRY_POINT)
    suspend fun getExchangeInfo(
        @Query("authkey") authkey: String,
        @Query("searchdata") date: String,
        @Query("data") data: String,
    ): List<ExchangeInfoEntity>
}