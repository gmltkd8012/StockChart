package com.leecoder.data.repository

import com.leecoder.data.source.KoreaAeximDataSource
import com.leecoder.network.api.KoreaAeximApi
import com.leecoder.network.entity.toDataList
import com.leecoder.stockchart.appconfig.BuildConfig
import com.leecoder.stockchart.model.exchange.ExchangeInfoData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KoreaAeximRepositoryImpl @Inject constructor(
    private val koreaAeximDataSource: KoreaAeximDataSource,
): KoreaAeximRepository {

    override suspend fun getExchangeInfo(): Flow<List<ExchangeInfoData>> = koreaAeximDataSource.getExchangeInfo()
}