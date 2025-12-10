package com.leecoder.data.repository

import com.leecoder.network.AppDispatchers
import com.leecoder.network.Dispatcher
import com.leecoder.network.source.KisInvestmentOverseasDataSource
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.model.stock.CurrentPriceNasdaqData
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
        symb: String
    ): Flow<CurrentPriceNasdaqData> {
        val token = getAuthToken()

        return kisInvestmentOverseasDataSource
            .getCurrentPriceNasdaq(
                token = token,
                auth = "",
                excd = "NAS",
                symb
            ).map { result -> result.getOrThrow() }
    }

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