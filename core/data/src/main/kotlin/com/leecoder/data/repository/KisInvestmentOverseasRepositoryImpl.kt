package com.leecoder.data.repository

import com.leecoder.data.source.KisInvestmentOverseasDataSource
import com.leecoder.network.entity.CurrentPriceNasdaqResponse
import com.leecoder.stockchart.model.stock.CurrentPriceNasdaqData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KisInvestmentOverseasRepositoryImpl @Inject constructor(
    private val kisInvestmentOverseasDataSource: KisInvestmentOverseasDataSource
): KisInvestmentOverseasRepository {

    override suspend fun getCurrentPriceNasdaq(
        auth: String,
        excd: String,
        symb: String
    ): Flow<CurrentPriceNasdaqData> = kisInvestmentOverseasDataSource
        .getCurrentPriceNasdaq(auth, excd, symb)
        .map { result -> result.getOrThrow() }

    override suspend fun getTimeItemChartPriceNasdaq(
        auth: String,
        excd: String,
        symb: String,
        nmin: String,
        pinc: String,
        next: String,
        nrec: String,
        fill: String,
        keyb: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }
}