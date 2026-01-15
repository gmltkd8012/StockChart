package com.leecoder.stockchart

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.leecoder.stockchart.work.scheduler.CurrencyScheduler
import com.leecoder.stockchart.work.scheduler.NasdaqTradeCodeScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class StockChartApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var nasdaqTradeCodeScheduler: NasdaqTradeCodeScheduler

    @Inject
    lateinit var currencyScheduler: CurrencyScheduler

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        initNasdaqTradeCodeScheduler()
        initCurrencyScheduler()
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

    /**
     * 환율 정보 자동 갱신 스케줄러 초기화
     * - 앱 시작 시 즉시 환율 정보 업데이트 (DataStore에 데이터가 없거나 오래된 경우 대비)
     * - 매일 11시에 자동 갱신 스케줄 등록 (한국수출입은행 환율 고시 시간)
     */
    private fun initCurrencyScheduler() {
        // 앱 시작 시 즉시 환율 정보 업데이트
        currencyScheduler.executeImmediately()

        // 매일 11시에 자동 갱신 스케줄 등록
        currencyScheduler.scheduleCurrencyUpdates()
    }
}