package com.leecoder.data.repository

import com.leecoder.stockchart.model.exchange.ExchangeInfoData

interface KoreaAeximRepository {

    /**
     * 특정 날짜의 환율 정보 조회
     * @param date 조회할 날짜 (YYYYMMDD 형식)
     */
    suspend fun getExchangeInfo(date: String): List<ExchangeInfoData>

    /**
     * 환율 정보 조회 (fallback 포함)
     * 당일 데이터가 없으면 전날 데이터를 조회
     * @param date 조회할 날짜 (YYYYMMDD 형식)
     * @param maxRetryDays 최대 재시도 일수 (기본 7일)
     */
    suspend fun getExchangeInfoWithFallback(
        date: String,
        maxRetryDays: Int = 7
    ): List<ExchangeInfoData>
}