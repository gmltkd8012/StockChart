package com.leecoder.stockchart.datastore.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    val currentKrInvestmentToken: Flow<String?>
    suspend fun refreshKrInvestmentToken(token: String)

    val currentKrInvestmentTokenExpired: Flow<Long?>
    suspend fun refreshKrInvestmentTokenExpired(tokenExpired: String)

    val currentKrInvestmentWebSocket: Flow<String?>
    suspend fun refreshKrInvestmentWebSocket(webSocket: String)
}