package com.leecoder.stockchart

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.leecoder.stockchart.work.scheduler.NasdaqTradeCodeScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class StockChartApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var nasdaqTradeCodeScheduler: NasdaqTradeCodeScheduler

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        initNasdaqTradeCodeScheduler()
    }

    /**
     * 나스닥 트레이드 코드 (주간/야간) 자동 갱신 스케줄러 초기화
     * - 앱 시작 시 현재 시간에 맞는 코드로 즉시 동기화
     * - 10:00, 18:00 에 자동 갱신 스케줄 등록
     */
    private fun initNasdaqTradeCodeScheduler() {
        // 앱 시작 시 현재 시간에 맞는 코드로 즉시 동기화
        nasdaqTradeCodeScheduler.executeImmediately()

        // 주간/야간 전환 시점에 자동 갱신 스케줄 등록
        nasdaqTradeCodeScheduler.scheduleTradeCodeUpdates()
    }
}