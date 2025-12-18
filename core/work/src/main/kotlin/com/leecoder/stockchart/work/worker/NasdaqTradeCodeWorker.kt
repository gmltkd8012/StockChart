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

        // 주간 시간대: 10:00 ~ 18:00
        const val DAY_START_HOUR = 10
        const val DAY_END_HOUR = 18

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
    }

    override suspend fun doWork(): Result {
        return try {
            val oldCode = dataStoreRepository.currentNasdaqTradeCode.first()
            val newCode = getTradeCodeByCurrentTime()

            Log.d(TAG, "Trade code check - old: $oldCode, new: $newCode")

            // 코드가 변경된 경우에만 업데이트
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

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update trade code", e)
            Result.failure()
        }
    }

    /**
     * 현재 시간에 따른 트레이드 코드 반환
     * 주간 (10:00 ~ 18:00): RBAQ
     * 야간 (그 외): DNAS
     */
    private fun getTradeCodeByCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

        return if (currentHour in DAY_START_HOUR until DAY_END_HOUR) {
            DataStoreConst.ValueConst.NASDAQ_DAY_CODE
        } else {
            DataStoreConst.ValueConst.NASDAQ_NIGHT_CODE
        }
    }
}
