package com.leecoder.network.api

import com.leecoder.network.entity.CurrentPriceNasdaqResponse
import com.leecoder.network.entity.overseas.ChartPriceResponse
import com.leecoder.stockchart.appconfig.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KisInvestmentOverseasApi {

    @GET(BuildConfig.KIS_NASDAQ_INQUIRE_CCNL)
    suspend fun getCurrentPriceNasdaq(
        @Header("content-type") contentType: String,
        @Header("authorization") authorization: String,
        @Header("appkey") appkey: String,
        @Header("appsecret") appsecret: String,
        @Header("tr_id") trId: String,
        @Query("AUTH") auth: String,
        @Query("EXCD") excd: String,
        @Query("SYMB") symb: String,
    ): CurrentPriceNasdaqResponse

    @GET(BuildConfig.KIS_NASDAQ_INQUIRE_TIME_ITEMCHARTPRICE)
    suspend fun getTimeItemChartPriceNasdaq(
        @Header("content-type") contentType: String,
        @Header("authorization") authorization: String,
        @Header("appkey") appkey: String,
        @Header("appsecret") appsecret: String,
        @Header("tr_id") trId: String,
        @Query("AUTH") auth: String,
        @Query("EXCD") excd: String,
        @Query("SYMB") symb: String,
        @Query("NMIN") nmin: String,
        @Query("PINC") pinc: String,
        @Query("NEXT") next: String,
        @Query("NREC") nrec: String,
        @Query("FILL") fill: String,
        @Query("KEYB") keyb: String,
    ): ChartPriceResponse
}