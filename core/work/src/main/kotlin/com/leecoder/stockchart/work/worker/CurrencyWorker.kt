package com.leecoder.stockchart.work.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.leecoder.data.repository.KoreaAeximRepository
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.model.exchange.ExchangeInfoData
import com.leecoder.stockchart.util.log.Logger
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 환율 정보 갱신 워커
 *
 * 한국수출입은행 API를 통해 USD 환율 정보를 가져와 DataStore에 저장합니다.
 * - 당일 데이터가 없는 경우 (11시 이전, 공휴일 등) 전날 데이터를 조회합니다.
 * - 최대 7일까지 이전 데이터를 조회합니다.
 */
@HiltWorker
class CurrencyWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val dataStoreRepository: DataStoreRepository,
    private val koreaAeximRepository: KoreaAeximRepository,
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val UNIQUE_WORK_NAME = "currency_worker"
        private const val TAG = "CurrencyWorker"
        private const val DATE_FORMAT = "yyyyMMdd"
        private const val USD_CURRENCY_UNIT = "USD"
    }

    override suspend fun doWork(): Result {
        return try {
            Logger.d(TAG, "Starting currency update work")

            val today = getTodayDateString()
            Logger.d(TAG, "Fetching exchange info for date: $today")

            // fallback 로직이 포함된 API 호출 (데이터 없으면 전날 데이터 조회)
            val exchangeInfoList = koreaAeximRepository.getExchangeInfoWithFallback(today)

            if (exchangeInfoList.isEmpty()) {
                Logger.w(TAG, "Failed to fetch exchange info - empty response after fallback")
                return Result.retry()
            }

            // USD 환율 정보 추출 및 저장
            val usdExchangeInfo = findUsdExchangeInfo(exchangeInfoList)
            if (usdExchangeInfo != null) {
                saveExchangeRate(usdExchangeInfo)
                Logger.d(TAG, "Successfully updated USD exchange rate: ${usdExchangeInfo.dealBasR}")
                Result.success()
            } else {
                Logger.w(TAG, "USD exchange info not found in response")
                Result.retry()
            }
        } catch (e: Exception) {
            Logger.e(TAG, "Failed to update exchange rate", e)
            Result.retry()
        }
    }

    /**
     * 오늘 날짜를 YYYYMMDD 형식으로 반환
     */
    private fun getTodayDateString(): String {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.KOREA)
        return dateFormat.format(Date())
    }

    /**
     * 환율 정보 리스트에서 USD 환율 정보 추출
     */
    private fun findUsdExchangeInfo(exchangeInfoList: List<ExchangeInfoData>): ExchangeInfoData? {
        return exchangeInfoList.find { it.curUnit == USD_CURRENCY_UNIT }
    }

    /**
     * USD 환율 정보를 DataStore에 저장
     */
    private suspend fun saveExchangeRate(usdExchangeInfo: ExchangeInfoData) {
        // 매매기준율 저장 (콤마 제거)
        val exchangeRate = usdExchangeInfo.dealBasR.replace(",", "")
        dataStoreRepository.updateExchangeRateUsd(exchangeRate)

        Logger.d("Exchange rate saved - USD: $exchangeRate")
    }
}
