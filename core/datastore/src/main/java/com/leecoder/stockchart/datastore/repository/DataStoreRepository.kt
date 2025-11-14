package com.leecoder.stockchart.datastore.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    val currentKrInvestmentToken: Flow<String?>
    suspend fun refreshKrInvestmentToken(token: String)

    val currentKrInvestmentTokenExpired: Flow<Long?>
    suspend fun refreshKrInvestmentTokenExpired(tokenExpired: String)

    val currentKrInvestmentWebSocket: Flow<String?>
    suspend fun refreshKrInvestmentWebSocket(webSocket: String)

    val currentBollingerSetting: Flow<String>
    suspend fun updateBollingerSetting(value: String)

    val currentMarketInfo: Flow<String>
    suspend fun updateMarketInfo(marketId: String)

    val currentExchangeRateSaveTime: Flow<Long>
    suspend fun updateExchangeRateSaveTime(saveTime: Long)
}