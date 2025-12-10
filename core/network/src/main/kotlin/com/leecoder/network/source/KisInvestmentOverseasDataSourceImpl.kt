package com.leecoder.network.source

import android.util.Log
import com.leecoder.network.api.KisInvestmentOverseasApi
import com.leecoder.network.entity.toData
import com.leecoder.network.sync.suspendRunCatching
import com.leecoder.stockchart.appconfig.BuildConfig
import com.leecoder.stockchart.model.stock.CurrentPriceNasdaqData
import com.leecoder.stockchart.model.stock.TimeItemChartPriceNasdaqDetailData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class KisInvestmentOverseasDataSourceImpl @Inject constructor(
    private val kisInvestmentOverseasApi: KisInvestmentOverseasApi,
): KisInvestmentOverseasDataSource {

    companion object {
        const val SUCCESS_CODE = "0"
        const val TRID = "HHDFS00000300"
    }

    override suspend fun getCurrentPriceNasdaq(
        token: String,
        auth: String,
        excd: String,
        symb: String
    ): Flow<Result<CurrentPriceNasdaqData>> = flow {
        val result = suspendRunCatching {
            kisInvestmentOverseasApi.getCurrentPriceNasdaq(
                contentType = "application/json; charset=utf-8",
                authorization = token,
                appkey = BuildConfig.AppKey,
                appsecret = BuildConfig.AppSecret,
                trId = TRID,
                auth = auth,
                excd = excd,
                symb = symb
            ).output.toData()
        }

        emit(result)
    }

    override suspend fun getTimeItemChartPriceNasdaqDetail(
        token: String,
        auth: String,
        excd: String,
        symb: String,
        nmin: String,
        pinc: String,
        next: String,
        nrec: String,
        fill: String,
        keyb: String
    ): Flow<Result<TimeItemChartPriceNasdaqDetailData>> = flow {
        val result = suspendRunCatching {
            kisInvestmentOverseasApi.getTimeItemChartPriceNasdaq(
                contentType = "application/json; charset=utf-8",
                authorization = token,
                appkey = BuildConfig.AppKey,
                appsecret = BuildConfig.AppSecret,
                trId = TRID,
                auth = auth,
                excd = excd,
                symb = symb,
                nmin = nmin,
                pinc = pinc,
                next = next,
                nrec = nrec,
                fill = fill,
                keyb = keyb
            )
        }

        emit(Result.failure(Exception("zz")))
    }
}
