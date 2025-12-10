package com.leecoder.data.repository

import com.leecoder.stockchart.model.stock.CurrentPriceNasdaqData
import kotlinx.coroutines.flow.Flow

interface KisInvestmentOverseasRepository {

    suspend fun getCurrentPriceNasdaq(
        symb: String,
    ): Flow<CurrentPriceNasdaqData>

    suspend fun getTimeItemChartPriceNasdaq(
        auth: String,
        excd: String,
        symb: String,
        nmin: String,
        pinc: String,
        next: String,
        nrec: String,
        fill: String,
        keyb: String,
    ): Result<Unit>
}