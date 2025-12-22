package com.leecoder.network.source

import android.util.Log
import com.leecoder.network.api.KisInvestmentOverseasApi
import com.leecoder.network.entity.overseas.toDataList
import com.leecoder.network.entity.toData
import com.leecoder.network.sync.suspendRunCatching
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.model.request.ChartPriceRequest
import com.leecoder.stockchart.model.request.CurrentPriceRequest
import com.leecoder.stockchart.model.stock.ChartPriceData
import com.leecoder.stockchart.model.stock.CurrentPriceData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KisInvestmentOverseasDataSourceImpl @Inject constructor(
    private val kisInvestmentOverseasApi: KisInvestmentOverseasApi,
    private val dataStoreRepository: DataStoreRepository,
): KisInvestmentOverseasDataSource {

    companion object {
        const val CONTENT_TYPE = "application/json; charset=utf-8"
        const val SUCCESS_CODE = "0"
        const val TRID_CURRENT_PRICE = "HHDFS00000300"
        const val TRID_CHART_PRICE = "HHDFS76950200"
    }

    override suspend fun getCurrentPriceNasdaq(
        request: CurrentPriceRequest
    ): Flow<Result<CurrentPriceData>> = flow {
        val appKey = dataStoreRepository.currentAppKey.first()
        val appSecret = dataStoreRepository.currentAppSecret.first()

        if (appKey == null || appSecret == null) {
            emit(Result.failure(IllegalStateException("AppKey or AppSecret is null")))
            return@flow
        }

        val result = suspendRunCatching {
            kisInvestmentOverseasApi.getCurrentPriceNasdaq(
                contentType = CONTENT_TYPE,
                authorization = request.authorization,
                appkey = appKey,
                appsecret = appSecret,
                trId = TRID_CURRENT_PRICE,
                auth = request.auth,
                excd = request.excd,
                symb = request.symb
            ).toData()
        }

        emit(result)
    }

    override suspend fun getChartPriceNasdaq(
        request: ChartPriceRequest
    ): Flow<Result<List<ChartPriceData>>> = flow {
        val appKey = dataStoreRepository.currentAppKey.first()
        val appSecret = dataStoreRepository.currentAppSecret.first()

        if (appKey == null || appSecret == null) {
            emit(Result.failure(IllegalStateException("AppKey or AppSecret is null")))
            return@flow
        }

        val result = suspendRunCatching {
            kisInvestmentOverseasApi.getTimeItemChartPriceNasdaq(
                contentType = CONTENT_TYPE,
                authorization = request.authorization,
                appkey = appKey,
                appsecret = appSecret,
                trId = TRID_CHART_PRICE,
                auth = request.auth,
                excd = request.excd,
                symb = request.symb,
                nmin = request.nmin,
                pinc = request.pinc,
                next = request.next,
                nrec = request.nrec,
                fill = request.fill,
                keyb = request.keyb
            ).toDataList()
        }

        emit(result)
    }
}
