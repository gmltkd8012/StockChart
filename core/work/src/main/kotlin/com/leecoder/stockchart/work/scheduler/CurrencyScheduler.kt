package com.leecoder.stockchart.work.scheduler

import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.leecoder.stockchart.work.worker.CurrencyWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 환율 정보 자동 갱신 스케줄러
 *
 * 한국수출입은행은 영업일 오전 11시에 당일 환율을 고시합니다.
 * 매일 11시에 환율 정보를 자동으로 갱신합니다.
 */
@Singleton
class CurrencyScheduler @Inject constructor(
    private val workManager: WorkManager,
) {
    companion object {
        private const val TAG = "CurrencyScheduler"

        // 환율 고시 시간: 오전 11시
        private const val EXCHANGE_RATE_UPDATE_HOUR = 11
        private const val EXCHANGE_RATE_UPDATE_MINUTE = 0

        private const val WORK_NAME_CURRENCY_UPDATE = "currency_update_work"
    }

    /**
     * 매일 11시에 환율 갱신 작업 스케줄 설정
     * 앱 시작 시 호출하여 백그라운드 작업을 예약합니다.
     */
    fun scheduleCurrencyUpdates() {
        Log.d(
            TAG,
            "Scheduling currency updates at $EXCHANGE_RATE_UPDATE_HOUR:${
                EXCHANGE_RATE_UPDATE_MINUTE.toString().padStart(2, '0')
            }"
        )

        val initialDelay = calculateDelayUntil(
            EXCHANGE_RATE_UPDATE_HOUR,
            EXCHANGE_RATE_UPDATE_MINUTE
        )

        val workRequest = PeriodicWorkRequestBuilder<CurrencyWorker>(
            24, TimeUnit.HOURS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .addTag(WORK_NAME_CURRENCY_UPDATE)
            .build()

        workManager.enqueueUniquePeriodicWork(
            WORK_NAME_CURRENCY_UPDATE,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

        Log.d(
            TAG,
            "$WORK_NAME_CURRENCY_UPDATE scheduled - target: $EXCHANGE_RATE_UPDATE_HOUR:${
                EXCHANGE_RATE_UPDATE_MINUTE.toString().padStart(2, '0')
            }, initial delay: ${initialDelay / 1000 / 60} minutes"
        )
    }

    /**
     * 지정된 시각까지의 지연 시간(밀리초) 계산
     * 이미 지난 시간이면 다음 날의 해당 시각까지의 시간 반환
     */
    private fun calculateDelayUntil(targetHour: Int, targetMinute: Int): Long {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, targetHour)
            set(Calendar.MINUTE, targetMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // 이미 지난 시간이면 다음 날로 설정
        if (target.before(now) || target == now) {
            target.add(Calendar.DAY_OF_YEAR, 1)
        }

        return target.timeInMillis - now.timeInMillis
    }

    /**
     * 스케줄된 작업 취소
     */
    fun cancelScheduledWork() {
        workManager.cancelUniqueWork(WORK_NAME_CURRENCY_UPDATE)
        Log.d(TAG, "Currency update work cancelled")
    }

    /**
     * 즉시 환율 정보 업데이트 실행
     * 앱 시작 시 DataStore에 저장된 데이터가 없거나 오래된 경우 사용
     */
    fun executeImmediately() {
        val workRequest = OneTimeWorkRequestBuilder<CurrencyWorker>()
            .build()

        workManager.enqueue(workRequest)
        Log.d(TAG, "Immediate currency update enqueued")
    }
}
