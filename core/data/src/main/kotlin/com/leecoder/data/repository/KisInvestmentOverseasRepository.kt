package com.leecoder.data.repository

import com.leecoder.stockchart.model.stock.ChartPriceData
import com.leecoder.stockchart.model.stock.CurrentPriceData
import kotlinx.coroutines.flow.Flow

interface KisInvestmentOverseasRepository {

    suspend fun getCurrentPriceNasdaq(
        excd: String,
        symb: String,
    ): Flow<CurrentPriceData>

    suspend fun getChartPriceNasdaq(
        excd: String,
        symb: String,
    ): Flow<List<ChartPriceData>>
}