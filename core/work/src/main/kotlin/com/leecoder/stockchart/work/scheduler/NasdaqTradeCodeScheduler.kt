package com.leecoder.stockchart.work.scheduler

import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.leecoder.stockchart.work.worker.NasdaqTradeCodeWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 나스닥 트레이드 코드 (주간/야간) 자동 갱신 스케줄러
 *
 * 주간: 10:00 ~ 18:00 -> RBAQ
 * 야간: 그 외 시간 -> DNAS
 *
 * 매일 10:00, 18:00에 트레이드 코드를 확인하고 필요시 업데이트합니다.
 */
@Singleton
class NasdaqTradeCodeScheduler @Inject constructor(
    private val workManager: WorkManager,
) {
    companion object {
        private const val TAG = "NasdaqTradeCodeScheduler"

        // 주간 시작: 10:00
        private const val DAY_START_HOUR = 10
        private const val DAY_START_MINUTE = 0

        // 야간 시작: 18:00
        private const val NIGHT_START_HOUR = 18
        private const val NIGHT_START_MINUTE = 0

        private const val WORK_NAME_DAY = "nasdaq_trade_code_day_work"
        private const val WORK_NAME_NIGHT = "nasdaq_trade_code_night_work"
    }

    /**
     * 주간/야간 트레이드 코드 갱신 스케줄 설정
     * 앱 시작 시 호출하여 백그라운드 작업을 예약합니다.
     */
    fun scheduleTradeCodeUpdates() {
        Log.d(TAG, "Scheduling trade code updates")

        scheduleDayTimeWork()
        scheduleNightTimeWork()
    }

    /**
     * 주간 시작 (10:00) 스케줄링
     */
    private fun scheduleDayTimeWork() {
        val initialDelay = calculateDelayUntil(DAY_START_HOUR, DAY_START_MINUTE)

        val workRequest = PeriodicWorkRequestBuilder<NasdaqTradeCodeWorker>(
            24, TimeUnit.HOURS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .addTag(WORK_NAME_DAY)
            .build()

        workManager.enqueueUniquePeriodicWork(
            WORK_NAME_DAY,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

        Log.d(TAG, "Day work scheduled - initial delay: ${initialDelay / 1000 / 60} minutes")
    }

    /**
     * 야간 시작 (18:00) 스케줄링
     */
    private fun scheduleNightTimeWork() {
        val initialDelay = calculateDelayUntil(NIGHT_START_HOUR, NIGHT_START_MINUTE)

        val workRequest = PeriodicWorkRequestBuilder<NasdaqTradeCodeWorker>(
            24, TimeUnit.HOURS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .addTag(WORK_NAME_NIGHT)
            .build()

        workManager.enqueueUniquePeriodicWork(
            WORK_NAME_NIGHT,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

        Log.d(TAG, "Night work scheduled - initial delay: ${initialDelay / 1000 / 60} minutes")
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
        workManager.cancelUniqueWork(WORK_NAME_DAY)
        workManager.cancelUniqueWork(WORK_NAME_NIGHT)
        Log.d(TAG, "Scheduled work cancelled")
    }

    /**
     * 즉시 트레이드 코드 업데이트 실행
     * 앱 시작 시 현재 시간에 맞는 코드로 즉시 동기화할 때 사용
     */
    fun executeImmediately() {
        val workRequest = androidx.work.OneTimeWorkRequestBuilder<NasdaqTradeCodeWorker>()
            .build()

        workManager.enqueue(workRequest)
        Log.d(TAG, "Immediate trade code update enqueued")
    }
}
