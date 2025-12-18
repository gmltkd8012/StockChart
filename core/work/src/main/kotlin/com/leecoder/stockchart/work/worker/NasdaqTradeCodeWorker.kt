package com.leecoder.stockchart.work.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.leecoder.data.source.WebSocketDataSource
import com.leecoder.stockchart.datastore.const.DataStoreConst
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.Calendar

@HiltWorker
class NasdaqTradeCodeWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val dataStoreRepository: DataStoreRepository,
    private val webSocketDataSource: WebSocketDataSource,
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val UNIQUE_WORK_NAME = "nasdaq_trade_code_worker"
        private const val TAG = "NasdaqTradeCodeWorker"

        // WebSocket 트레이드 코드 시간대
        const val DAY_START_HOUR = 10    // 주간 시작: 10:00
        const val DAY_END_HOUR = 18      // 주간 종료: 18:00

        // 시장 세션 시간대 (분 단위)
        const val DAY_TRADING_START = 10 * 60        // 주간거래 시작: 10:00 (600분)
        const val PRE_MARKET_START = 18 * 60         // 프리마켓 시작: 18:00 (1080분)
        const val REGULAR_START = 23 * 60 + 30       // 정규장 시작: 23:30 (1410분)
        const val AFTER_MARKET_START = 6 * 60        // 애프터마켓 시작: 06:00 (360분)

        /**
         * 주어진 시간(밀리초)에 따른 트레이드 코드 반환 (테스트용)
         * @param timeMillis 시간 (밀리초)
         * @return 트레이드 코드 (RBAQ 또는 DNAS)
         */
        fun getTradeCodeByTime(timeMillis: Long): String {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = timeMillis
            }
            val hour = calendar.get(Calendar.HOUR_OF_DAY)

            return if (hour in DAY_START_HOUR until DAY_END_HOUR) {
                DataStoreConst.ValueConst.NASDAQ_DAY_CODE
            } else {
                DataStoreConst.ValueConst.NASDAQ_NIGHT_CODE
            }
        }

        /**
         * 주어진 시간(밀리초)에 따른 시장 세션 반환 (테스트용)
         * @param timeMillis 시간 (밀리초)
         * @return 시장 세션 (day_trading, pre_market, regular, after_market)
         */
        fun getMarketSessionByTime(timeMillis: Long): String {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = timeMillis
            }
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val totalMinutes = hour * 60 + minute

            return when {
                // 주간거래: 10:00 ~ 18:00
                totalMinutes in DAY_TRADING_START until PRE_MARKET_START ->
                    DataStoreConst.ValueConst.SESSION_DAY_TRADING
                // 프리마켓: 18:00 ~ 23:30
                totalMinutes in PRE_MARKET_START until REGULAR_START ->
                    DataStoreConst.ValueConst.SESSION_PRE_MARKET
                // 정규장: 23:30 ~ 06:00 (익일)
                totalMinutes >= REGULAR_START || totalMinutes < AFTER_MARKET_START ->
                    DataStoreConst.ValueConst.SESSION_REGULAR
                // 애프터마켓: 06:00 ~ 10:00
                else -> DataStoreConst.ValueConst.SESSION_AFTER_MARKET
            }
        }
    }

    override suspend fun doWork(): Result {
        return try {
            // 1. WebSocket 트레이드 코드 업데이트
            updateTradeCodeIfNeeded()

            // 2. 시장 세션 상태 업데이트
            updateMarketSessionIfNeeded()

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update trade code or market session", e)
            Result.failure()
        }
    }

    /**
     * WebSocket 트레이드 코드 업데이트 (변경 시에만)
     */
    private suspend fun updateTradeCodeIfNeeded() {
        val oldCode = dataStoreRepository.currentNasdaqTradeCode.first()
        val newCode = getTradeCodeByCurrentTime()

        Log.d(TAG, "Trade code check - old: $oldCode, new: $newCode")

        if (oldCode != newCode) {
            Log.d(TAG, "Updating trade code from $oldCode to $newCode")

            // DataStore 업데이트
            dataStoreRepository.updateNasdaqTradeCode(newCode)

            // WebSocket 구독 갱신 (기존 구독 해제 후 새 코드로 재구독)
            webSocketDataSource.refreshSubscriptionsWithNewTradeCode(oldCode, newCode)

            Log.d(TAG, "Trade code updated successfully")
        } else {
            Log.d(TAG, "Trade code unchanged, skipping update")
        }
    }

    /**
     * 시장 세션 상태 업데이트 (변경 시에만)
     */
    private suspend fun updateMarketSessionIfNeeded() {
        val oldSession = dataStoreRepository.currentNasdaqMarketSession.first()
        val newSession = getMarketSessionByCurrentTime()

        Log.d(TAG, "Market session check - old: $oldSession, new: $newSession")

        if (oldSession != newSession) {
            Log.d(TAG, "Updating market session from $oldSession to $newSession")
            dataStoreRepository.updateNasdaqMarketSession(newSession)
            Log.d(TAG, "Market session updated successfully")
        } else {
            Log.d(TAG, "Market session unchanged, skipping update")
        }
    }

    /**
     * 현재 시간에 따른 트레이드 코드 반환
     * 주간 (10:00 ~ 18:00): RBAQ
     * 야간 (그 외): DNAS
     */
    private fun getTradeCodeByCurrentTime(): String {
        return getTradeCodeByTime(System.currentTimeMillis())
    }

    /**
     * 현재 시간에 따른 시장 세션 반환
     * 주간거래 (10:00 ~ 18:00)
     * 프리마켓 (18:00 ~ 23:30)
     * 정규장 (23:30 ~ 06:00)
     * 애프터마켓 (06:00 ~ 10:00)
     */
    private fun getMarketSessionByCurrentTime(): String {
        return getMarketSessionByTime(System.currentTimeMillis())
    }
}
