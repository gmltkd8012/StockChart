package com.leecoder.data.repository

import com.leecoder.network.entity.ExchangeInfoEntity
import com.leecoder.stockchart.model.exchange.ExchangeInfoData
import kotlinx.coroutines.flow.Flow

interface KoreaAeximRepository {

    suspend fun getExchangeInfo(): Flow<List<ExchangeInfoData>>
}