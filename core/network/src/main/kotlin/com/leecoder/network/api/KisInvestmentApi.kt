package com.leecoder.network.api

import com.leecoder.network.entity.CurrentPriceResponse
import com.leecoder.network.entity.DailyPriceResponse
import com.leecoder.network.entity.TimeItemChartPriceResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KisInvestmentApi {
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

    @GET("/uapi/domestic-stock/v1/quotations/inquire-ccnl")
    suspend fun getCurrentPrice(
        @Header("content-type") contentType: String?,
        @Header("authorization") authorization: String,
        @Header("appkey") appkey: String,
        @Header("appsecret") appsecret: String,
        @Header("tr_id") trId: String,
        @Header("custtype") custtype: String,
        @Query("FID_COND_MRKT_DIV_CODE") mrktDivCode: String,
        @Query("FID_INPUT_ISCD") inputIscd: String,
    ): CurrentPriceResponse

    @GET("/uapi/domestic-stock/v1/quotations/inquire-time-itemchartprice")
    suspend fun getTimeItemChartPrice(
        @Header("content-type") contentType: String?,
        @Header("authorization") authorization: String,
        @Header("appkey") appkey: String,
        @Header("appsecret") appsecret: String,
        @Header("tr_id") trId: String,
        @Header("custtype") custtype: String,
        @Query("FID_COND_MRKT_DIV_CODE") mrktDivCode: String,
        @Query("FID_INPUT_ISCD") inputIscd: String,
        @Query("FID_INPUT_HOUR_1") inputHour: String, // 입력 시간1
        @Query("FID_PW_DATA_INCU_YN") incuYn: String, // 과거 데이터 포함 여부
        @Query("FID_ETC_CLS_CODE") clsCode: String, // 기타 구분 코드
    ): TimeItemChartPriceResponse
}