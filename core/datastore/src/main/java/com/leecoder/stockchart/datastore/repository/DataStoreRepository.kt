package com.leecoder.stockchart.datastore.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    val currentKrInvestmentToken: Flow<String?>
    suspend fun refreshKrInvestmentToken(token: String)


    val currentKrInvestmentTokenExpired: Flow<String?>
    suspend fun refreshKrInvestmentTokenExpired(tokenExpired: String)

}