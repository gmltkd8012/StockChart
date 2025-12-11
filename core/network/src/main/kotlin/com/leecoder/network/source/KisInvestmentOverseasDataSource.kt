package com.leecoder.network.source

import com.leecoder.stockchart.model.request.ChartPriceRequest
import com.leecoder.stockchart.model.request.CurrentPriceRequest
import com.leecoder.stockchart.model.stock.ChartPriceData
import com.leecoder.stockchart.model.stock.CurrentPriceData
import kotlinx.coroutines.flow.Flow

interface KisInvestmentOverseasDataSource {

    suspend fun getCurrentPriceNasdaq(request: CurrentPriceRequest): Flow<Result<CurrentPriceData>>

    suspend fun getChartPriceNasdaq(request: ChartPriceRequest): Flow<Result<List<ChartPriceData>>>
}