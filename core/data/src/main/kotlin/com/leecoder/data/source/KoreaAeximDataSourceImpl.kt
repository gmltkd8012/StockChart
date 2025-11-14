package com.leecoder.data.source

import com.leecoder.network.api.KoreaAeximApi
import com.leecoder.network.entity.toDataList
import com.leecoder.stockchart.appconfig.BuildConfig
import com.leecoder.stockchart.appconfig.config.AppConfig
import com.leecoder.stockchart.model.exchange.ExchangeInfoData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KoreaAeximDataSourceImpl @Inject constructor(
    private val appConfig: AppConfig,
    private val koreaAeximApi: KoreaAeximApi
): KoreaAeximDataSource {

    override suspend fun getExchangeInfo(): Flow<List<ExchangeInfoData>> =
        flow {
            emit(
                koreaAeximApi.getExchangeInfo(
                    authkey = appConfig.korea_aexim_api_key,
                    date = "2025-11-14",
                    data = "AP01"
                ).toDataList()
            )
        }
}