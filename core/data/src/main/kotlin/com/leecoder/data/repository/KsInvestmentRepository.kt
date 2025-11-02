package com.leecoder.data.repository

import com.leecoder.stockchart.model.stock.CurrentPriceData
import com.leecoder.stockchart.model.stock.DailyPriceData
import kotlinx.coroutines.flow.Flow

interface KsInvestmentRepository {

    suspend fun getDailyPrice(
        iscd: String,
        periodCode: String,
    ): Flow<List<DailyPriceData>>

    suspend fun getCurrentPrice(
        iscd: String,
    ): Flow<CurrentPriceData>
}