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
 * 나스닥 트레이드 코드 및 시장 세션 자동 갱신 스케줄러
 *
 * WebSocket 트레이드 코드:
 * - 주간: 10:00 ~ 18:00 -> RBAQ
 * - 야간: 그 외 시간 -> DNAS
 *
 * 시장 세션 (UI 표시용):
 * - 주간거래: 10:00 ~ 18:00
 * - 프리마켓: 18:00 ~ 23:30
 * - 정규장: 23:30 ~ 06:00 (익일)
 * - 애프터마켓: 06:00 ~ 10:00
 *
 * 각 시장 세션 전환 시점에 자동 갱신됩니다.
 */
@Singleton
class NasdaqTradeCodeScheduler @Inject constructor(
    private val workManager: WorkManager,
) {
    companion object {
        private const val TAG = "NasdaqTradeCodeScheduler"

        // 주간거래 시작: 10:00
        private const val DAY_TRADING_START_HOUR = 10
        private const val DAY_TRADING_START_MINUTE = 0

        // 프리마켓 시작: 18:00
        private const val PRE_MARKET_START_HOUR = 18
        private const val PRE_MARKET_START_MINUTE = 0

        // 정규장 시작: 23:30
        private const val REGULAR_START_HOUR = 23
        private const val REGULAR_START_MINUTE = 30

        // 애프터마켓 시작: 06:00
        private const val AFTER_MARKET_START_HOUR = 6
        private const val AFTER_MARKET_START_MINUTE = 0

        private const val WORK_NAME_DAY_TRADING = "nasdaq_day_trading_work"
        private const val WORK_NAME_PRE_MARKET = "nasdaq_pre_market_work"
        private const val WORK_NAME_REGULAR = "nasdaq_regular_work"
        private const val WORK_NAME_AFTER_MARKET = "nasdaq_after_market_work"
    }

    /**
     * 시장 세션 전환 시점에 자동 갱신 스케줄 설정
     * 앱 시작 시 호출하여 백그라운드 작업을 예약합니다.
     */
    fun scheduleTradeCodeUpdates() {
        Log.d(TAG, "Scheduling market session updates")

        scheduleDayTradingWork()
        schedulePreMarketWork()
        scheduleRegularWork()
        scheduleAfterMarketWork()
    }

    /**
     * 주간거래 시작 (10:00) 스케줄링
     */
    private fun scheduleDayTradingWork() {
        schedulePeriodicWork(
            workName = WORK_NAME_DAY_TRADING,
            hour = DAY_TRADING_START_HOUR,
            minute = DAY_TRADING_START_MINUTE
        )
    }

    /**
     * 프리마켓 시작 (18:00) 스케줄링
     */
    private fun schedulePreMarketWork() {
        schedulePeriodicWork(
            workName = WORK_NAME_PRE_MARKET,
            hour = PRE_MARKET_START_HOUR,
            minute = PRE_MARKET_START_MINUTE
        )
    }

    /**
     * 정규장 시작 (23:30) 스케줄링
     */
    private fun scheduleRegularWork() {
        schedulePeriodicWork(
            workName = WORK_NAME_REGULAR,
            hour = REGULAR_START_HOUR,
            minute = REGULAR_START_MINUTE
        )
    }

    /**
     * 애프터마켓 시작 (06:00) 스케줄링
     */
    private fun scheduleAfterMarketWork() {
        schedulePeriodicWork(
            workName = WORK_NAME_AFTER_MARKET,
            hour = AFTER_MARKET_START_HOUR,
            minute = AFTER_MARKET_START_MINUTE
        )
    }

    /**
     * 공통 주기적 작업 스케줄링
     */
    private fun schedulePeriodicWork(workName: String, hour: Int, minute: Int) {
        val initialDelay = calculateDelayUntil(hour, minute)

        val workRequest = PeriodicWorkRequestBuilder<NasdaqTradeCodeWorker>(
            24, TimeUnit.HOURS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .addTag(workName)
            .build()

        workManager.enqueueUniquePeriodicWork(
            workName,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

        Log.d(
            TAG,
            "$workName scheduled - target: $hour:${
                minute.toString().padStart(2, '0')
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
        workManager.cancelUniqueWork(WORK_NAME_DAY_TRADING)
        workManager.cancelUniqueWork(WORK_NAME_PRE_MARKET)
        workManager.cancelUniqueWork(WORK_NAME_REGULAR)
        workManager.cancelUniqueWork(WORK_NAME_AFTER_MARKET)
        Log.d(TAG, "All scheduled work cancelled")
    }

    /**
     * 즉시 트레이드 코드 및 시장 세션 업데이트 실행
     * 앱 시작 시 현재 시간에 맞는 상태로 즉시 동기화할 때 사용
     */
    fun executeImmediately() {
        val workRequest = androidx.work.OneTimeWorkRequestBuilder<NasdaqTradeCodeWorker>()
            .build()

        workManager.enqueue(workRequest)
        Log.d(TAG, "Immediate trade code and market session update enqueued")
    }
}
