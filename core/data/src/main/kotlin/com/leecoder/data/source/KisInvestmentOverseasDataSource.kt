package com.leecoder.data.source

import com.leecoder.stockchart.model.stock.CurrentPriceNasdaqData
import com.leecoder.stockchart.model.stock.TimeItemChartPriceNasdaqDetailData
import kotlinx.coroutines.flow.Flow

interface KisInvestmentOverseasDataSource {

    suspend fun getCurrentPriceNasdaq(
        auth: String,
        excd: String,
        symb: String,
    ): Flow<Result<CurrentPriceNasdaqData>>

    suspend fun getTimeItemChartPriceNasdaqDetail(
        auth: String,
        excd: String,
        symb: String,
        nmin: String,
        pinc: String,
        next: String,
        nrec: String,
        fill: String,
        keyb: String,
    ): Flow<Result<TimeItemChartPriceNasdaqDetailData>>
}