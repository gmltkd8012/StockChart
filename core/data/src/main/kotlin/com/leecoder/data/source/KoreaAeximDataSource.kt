package com.leecoder.data.source

import com.leecoder.stockchart.model.exchange.ExchangeInfoData
import kotlinx.coroutines.flow.Flow

interface KoreaAeximDataSource {

    suspend fun getExchangeInfo(): Flow<List<ExchangeInfoData>>
}