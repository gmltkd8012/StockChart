package com.leecoder.data.repository

import com.leecoder.network.AppDispatchers
import com.leecoder.network.Dispatcher
import com.leecoder.network.source.KisInvestmentOverseasDataSource
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.model.request.ChartPriceRequest
import com.leecoder.stockchart.model.request.CurrentPriceRequest
import com.leecoder.stockchart.model.stock.ChartPriceData
import com.leecoder.stockchart.model.stock.CurrentPriceData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KisInvestmentOverseasRepositoryImpl @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val kisInvestmentOverseasDataSource: KisInvestmentOverseasDataSource,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
): KisInvestmentOverseasRepository {

    private suspend fun getAuthToken(): String =
        dataStoreRepository.currentKrInvestmentToken.first() ?: throw Exception("Token is Null")

    override suspend fun getCurrentPriceNasdaq(
        excd: String,
        symb: String,
    ): Flow<CurrentPriceData> {
        return kisInvestmentOverseasDataSource
            .getCurrentPriceNasdaq(
                CurrentPriceRequest(
                    authorization = getAuthToken(),
                    excd = excd,
                    symb = symb,
                )
            )
            .map { result -> result.getOrThrow() }
            .flowOn(ioDispatcher)
    }

    override suspend fun getChartPriceNasdaq(
        excd: String,
        symb: String,
    ): Flow<List<ChartPriceData>> =
        kisInvestmentOverseasDataSource
            .getChartPriceNasdaq(
                ChartPriceRequest(
                    authorization = getAuthToken(),
                    excd = excd,
                    symb = symb,
                )
            )
            .map { result -> result.getOrThrow() }
            .flowOn(ioDispatcher)
}