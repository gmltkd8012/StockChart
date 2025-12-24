package com.leecoder.data.repository

import com.leecoder.data.source.KoreaAeximDataSource
import com.leecoder.network.AppDispatchers
import com.leecoder.network.Dispatcher
import com.leecoder.stockchart.model.exchange.ExchangeInfoData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KoreaAeximRepositoryImpl @Inject constructor(
    private val koreaAeximDataSource: KoreaAeximDataSource,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : KoreaAeximRepository {

    override suspend fun getExchangeInfo(date: String): List<ExchangeInfoData> =
        withContext(ioDispatcher) {
            koreaAeximDataSource.getExchangeInfo(date)
        }

    override suspend fun getExchangeInfoWithFallback(
        date: String,
        maxRetryDays: Int
    ): List<ExchangeInfoData> =
        withContext(ioDispatcher) {
            koreaAeximDataSource.getExchangeInfoWithFallback(date, maxRetryDays)
        }
}