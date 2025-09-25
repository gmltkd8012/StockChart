package com.leecoder.network.api

import com.leecoder.data.Chart
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LiveChartApi {

    @GET("v8/finance/chart/{symbol}")
    suspend fun getLiveChart(
        @Path("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("range") range: String,
    ): Response<Chart>


}