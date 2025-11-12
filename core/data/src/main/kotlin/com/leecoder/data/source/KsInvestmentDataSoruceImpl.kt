package com.leecoder.data.source

import android.util.Log
import com.leecoder.network.api.KisInvestmentApi
import com.leecoder.network.entity.DailyPriceResponse
import com.leecoder.network.entity.KisInvestmentRquestHeader
import com.leecoder.network.entity.toData
import com.leecoder.stockchart.appconfig.BuildConfig
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.model.stock.CurrentPriceData
import com.leecoder.stockchart.model.stock.DailyPriceData
import com.leecoder.stockchart.model.stock.TimeItemChartPriceData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okhttp3.Callback
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named

class KsInvestmentDataSoruceImpl @Inject constructor(
    @Named("stock") private val client: OkHttpClient,
    private val kisInvestmentApi: KisInvestmentApi,
    private val datsStoreRepository: DataStoreRepository,
): KsInvestmentDataSource {

    override suspend fun getDailyPrice(
        iscd: String,
        periodCode: String
    ): Flow<List<DailyPriceData>> {
        val authorization = datsStoreRepository.currentKrInvestmentToken.first()

        if (authorization == null) {
            Log.e("[LeeCode]", "Error : Token is null")
            return flow { emit(emptyList()) }
        }

        val requestHeader = KisInvestmentRquestHeader(
            contentType = "application/json; charset=utf-8",
            authorization = authorization,
            appkey = BuildConfig.AppKey,
            appsecret = BuildConfig.AppSecret,
            personalseckeypkey = null,
            trId = "FHKST01010400",
            trCont = null,
            custtype = "P",
            seqNo = null,
            macAddress = null,
            phoneNumber = null,
            ipAddr = null,
            gtUid = null,
        )

        return flow {
            emit (
                kisInvestmentApi.getDailyPrice(
                    contentType = requestHeader.contentType,
                    authorization = requestHeader.authorization,
                    appkey = requestHeader.appkey,
                    appsecret = requestHeader.appsecret,
                    trId = requestHeader.trId,
                    custtype = requestHeader.custtype,
                    mrktDivCode = "J",
                    inputIscd = iscd,
                    divCode = periodCode,
                    prc = "1",
                ).output.map { it.toData() }
            )
        }
    }

    override suspend fun getCurrentPrice(iscd: String): Flow<CurrentPriceData> {
        val authorization = datsStoreRepository.currentKrInvestmentToken.first()

        if (authorization == null) {
            Log.e("[LeeCode]", "Error : Token is null")
            return flow { emit(CurrentPriceData(stckPrpr = "-1")) }
        }

        val requestHeader = KisInvestmentRquestHeader(
            contentType = "application/json; charset=utf-8",
            authorization = authorization,
            appkey = BuildConfig.AppKey,
            appsecret = BuildConfig.AppSecret,
            personalseckeypkey = null,
            trId = "FHKST01010300",
            trCont = null,
            custtype = "P",
            seqNo = null,
            macAddress = null,
            phoneNumber = null,
            ipAddr = null,
            gtUid = null,
        )

        return flow {
            emit (
                kisInvestmentApi.getCurrentPrice(
                    contentType = requestHeader.contentType,
                    authorization = requestHeader.authorization,
                    appkey = requestHeader.appkey,
                    appsecret = requestHeader.appsecret,
                    trId = requestHeader.trId,
                    custtype = requestHeader.custtype,
                    mrktDivCode = "J",
                    inputIscd = iscd,
                ).output.first().toData()
            )
        }
    }

    override suspend fun getTimeItemChartPrice(
        iscd: String
    ): Flow<List<TimeItemChartPriceData>> {
        val authorization = datsStoreRepository.currentKrInvestmentToken.first()

        if (authorization == null) {
            Log.e("[LeeCode]", "Error : Token is null")
            return flow { emit(emptyList()) }
        }

        val requestHeader = KisInvestmentRquestHeader(
            contentType = "application/json; charset=utf-8",
            authorization = authorization,
            appkey = BuildConfig.AppKey,
            appsecret = BuildConfig.AppSecret,
            personalseckeypkey = null,
            trId = "FHKST03010200",
            trCont = null,
            custtype = "P",
            seqNo = null,
            macAddress = null,
            phoneNumber = null,
            ipAddr = null,
            gtUid = null,
        )

        return flow {
            emit(
                kisInvestmentApi.getTimeItemChartPrice(
                    contentType = requestHeader.contentType,
                    authorization = requestHeader.authorization,
                    appkey = requestHeader.appkey,
                    appsecret = requestHeader.appsecret,
                    trId = requestHeader.trId,
                    custtype = requestHeader.custtype,
                    mrktDivCode = "J",
                    inputIscd = iscd,
                    inputHour = "Y",
                    incuYn = "Y",
                    clsCode = "Y",
                ).output2.map { it.toData() }
            )
        }
    }
}