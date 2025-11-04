package com.leecoder.data.repository

import com.leecoder.data.source.KsInvestmentDataSource
import com.leecoder.stockchart.model.stock.CurrentPriceData
import com.leecoder.stockchart.model.stock.DailyPriceData
import com.leecoder.stockchart.model.stock.TimeItemChartPriceData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KsInvestmentRepositoryImpl @Inject constructor(
    private val ksInvestmentDataSource: KsInvestmentDataSource,
): KsInvestmentRepository {

    override suspend fun getDailyPrice(
        iscd: String,
        periodCode: String
    ): Flow<List<DailyPriceData>> = ksInvestmentDataSource.getDailyPrice(iscd, periodCode)

    override suspend fun getCurrentPrice(
        iscd: String
    ): Flow<CurrentPriceData> = ksInvestmentDataSource.getCurrentPrice(iscd)

    override suspend fun getTimeItemChartPrice(
        iscd: String
    ): Flow<List<TimeItemChartPriceData>> = ksInvestmentDataSource.getTimeItemChartPrice(iscd)
}