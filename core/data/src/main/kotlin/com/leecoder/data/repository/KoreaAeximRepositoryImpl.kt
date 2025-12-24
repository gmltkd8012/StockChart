package com.leecoder.data.repository

import com.leecoder.data.source.KoreaAeximDataSource
import com.leecoder.stockchart.model.exchange.ExchangeInfoData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KoreaAeximRepositoryImpl @Inject constructor(
    private val koreaAeximDataSource: KoreaAeximDataSource,
) : KoreaAeximRepository {

    override suspend fun getExchangeInfo(date: String): List<ExchangeInfoData> =
        koreaAeximDataSource.getExchangeInfo(date)

    override suspend fun getExchangeInfoWithFallback(
        date: String,
        maxRetryDays: Int
    ): List<ExchangeInfoData> =
        koreaAeximDataSource.getExchangeInfoWithFallback(date, maxRetryDays)
}