package com.leecoder.data.source

import android.util.Log
import com.leecoder.network.api.KoreaAeximApi
import com.leecoder.network.entity.toDataList
import com.leecoder.stockchart.appconfig.config.AppConfig
import com.leecoder.stockchart.model.exchange.ExchangeInfoData
import com.leecoder.stockchart.util.log.Logger
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KoreaAeximDataSourceImpl @Inject constructor(
    private val appConfig: AppConfig,
    private val koreaAeximApi: KoreaAeximApi
) : KoreaAeximDataSource {

    companion object {
        private const val TAG = "KoreaAeximDataSource"
        private const val DATE_FORMAT = "yyyyMMdd"
    }

    override suspend fun getExchangeInfo(date: String): List<ExchangeInfoData> {
        return try {
            koreaAeximApi.getExchangeInfo(
                authkey = appConfig.korea_aexim_api_key,
                date = date,
                data = "AP01"
            ).toDataList()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch exchange info for date: $date", e)
            emptyList()
        }
    }

    override suspend fun getExchangeInfoWithFallback(
        date: String,
        maxRetryDays: Int
    ): List<ExchangeInfoData> {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.KOREA)
        val calendar = Calendar.getInstance().apply {
            time = dateFormat.parse(date) ?: return emptyList()
        }

        repeat(maxRetryDays) { attempt ->
            val currentDate = dateFormat.format(calendar.time)
            Logger.d("Fetching exchange info for date: $currentDate (attempt: ${attempt + 1})")

            val result = getExchangeInfo(currentDate)
            if (result.isNotEmpty()) {
                Logger.d("Successfully fetched exchange info for date: $currentDate")
                return result
            }

            // 데이터가 없으면 전날로 이동
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            Logger.d("No data for $currentDate, trying previous day")
        }

        Logger.d("Failed to fetch exchange info after $maxRetryDays attempts")
        return emptyList()
    }
}