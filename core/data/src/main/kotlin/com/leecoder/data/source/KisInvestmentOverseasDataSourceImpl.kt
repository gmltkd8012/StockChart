package com.leecoder.data.source

import android.util.Log
import com.leecoder.network.api.KisInvestmentOverseasApi
import com.leecoder.network.entity.toData
import com.leecoder.stockchart.appconfig.BuildConfig
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.model.stock.CurrentPriceNasdaqData
import com.leecoder.stockchart.model.stock.TimeItemChartPriceNasdaqDetailData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.JsonNull.content
import javax.inject.Inject

class KisInvestmentOverseasDataSourceImpl @Inject constructor(
    private val kisInvestmentOverseasApi: KisInvestmentOverseasApi,
    private val dataStoreRepository: DataStoreRepository,
): KisInvestmentOverseasDataSource {

    companion object {
        const val SUCCESS_CODE = "0"
    }

    override suspend fun getCurrentPriceNasdaq(
        auth: String,
        excd: String,
        symb: String
    ): Flow<Result<CurrentPriceNasdaqData>> {
        val authorization = dataStoreRepository.currentKrInvestmentToken.first()

        if (authorization == null) {
            return flow { emit(Result.failure(Exception("Token is null"))) }
        }

        val response = try {
            kisInvestmentOverseasApi.getCurrentPriceNasdaq(
                contentType = "application/json; charset=utf-8",
                authorization = authorization,
                appkey = BuildConfig.AppKey,
                appsecret = BuildConfig.AppSecret,
                trId = "HHDFS00000300",
                auth = auth,
                excd = excd,
                symb = symb
            )
        } catch (e: Exception) {
            Log.e("[LeeCoder]", "Error fetching current price Nasdaq: ${e.message}")
            return flow { emit(Result.failure(e)) }
        }

        return if (response.rtcd == SUCCESS_CODE) {
            flow { emit(Result.success(response.output.toData())) }
        } else {
            flow { emit(Result.failure(Throwable("Error rtCd -> ${response.rtcd}"))) }
        }
    }

    override suspend fun getTimeItemChartPriceNasdaqDetail(
        auth: String,
        excd: String,
        symb: String,
        nmin: String,
        pinc: String,
        next: String,
        nrec: String,
        fill: String,
        keyb: String
    ): Flow<Result<TimeItemChartPriceNasdaqDetailData>> {
        return flow { emptyFlow<TimeItemChartPriceNasdaqDetailData>() }
    }
}
