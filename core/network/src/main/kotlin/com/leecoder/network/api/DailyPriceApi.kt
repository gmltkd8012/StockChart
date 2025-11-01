package com.leecoder.network.api

import com.leecoder.network.entity.DailyPriceResponse
import com.leecoder.network.entity.DailyPriceRquestHeader
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface DailyPriceApi {
    @GET("/uapi/domestic-stock/v1/quotations/inquire-daily-price")
    suspend fun getDailyPrice(
        @Header("content-type") contentType: String?,
        @Header("authorization") authorization: String,
        @Header("appkey") appkey: String,
        @Header("appsecret") appsecret: String,
        @Header("tr_id") trId: String,
        @Header("custtype") custtype: String,
        @Query("FID_COND_MRKT_DIV_CODE") mrktDivCode: String,
        @Query("FID_INPUT_ISCD") inputIscd: String,
        @Query("FID_PERIOD_DIV_CODE") divCode: String,
        @Query("FID_ORG_ADJ_PRC") prc: String,
    ): DailyPriceResponse
}