package com.leecoder.stockchart.app.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.leecoder.data.repository.room.RoomDatabaseRepository
import com.leecoder.data.repository.stock.StockRepository
import com.leecoder.stockchart.domain.manager.BollingerManager
import com.leecoder.stockchart.domain.manager.BollingerManager.Companion
import com.leecoder.stockchart.domain.usecase.exchage.GetExchangeRateUsecase
import com.leecoder.stockchart.model.exchange.ExchangeRateData
import com.leecoder.stockchart.model.ui.NasdaqUiData
import com.leecoder.stockchart.ui.base.StateViewModel
import com.leecoder.stockchart.util.extension.convertToDouble
import com.leecoder.stockchart.util.log.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscribeViewModel @Inject constructor(
    private val stockRepository: StockRepository,
    private val roomDatabaseRepository: RoomDatabaseRepository,
    private val bollingerManager: BollingerManager,
    private val getExchangeRateUsecase: GetExchangeRateUsecase,
) : StateViewModel<SubscribeState, SubscribeSideEffect>(SubscribeState()) {

    companion object {
        private const val TAG = "SubscribeViewModel"
    }

    // 실시간 WebSocket 데이터 구독 (UI 표시용)
    // bollingerLowerAlertCodes에 있는 종목은 UI 업데이트에서 제외
    private val stockTickJob: Job =
        stockRepository.stocksUiFlow
            .onEach { ticks ->
                reduceState {
                    copy(tick = ticks)
                }
            }.launchIn(this@SubscribeViewModel)

    // 실시간 tick을 볼린저 계산에 전달
    private val bollingerTickJob: Job =
        stockRepository.tickFlow
            .onEach { tick ->
                val alertCode = bollingerManager.processTick(tick)
                if (alertCode != null) {
                    Logger.d("Bollinger lower alert: $alertCode")
                }
            }.launchIn(this@SubscribeViewModel)

    // 볼린저 하한가 달성 종목 구독
    private val bollingerAlertJob: Job =
        bollingerManager.bollingerLowerAlertCodes
            .onEach { alertCodes ->
                Logger.d("Bollinger lower alert codes: $alertCodes")
                reduceState {
                    copy(bollingerLowerAlertCodes = alertCodes.toList())
                }
            }.launchIn(this@SubscribeViewModel)

    /**
     *  ViewModel 초기화 작업
     */
    init {
        initExchangeRate()
        initBollingerManager()
    }

    // 환율 조회
    private fun initExchangeRate() {
        launch {
            val exchangeRates = getExchangeRateUsecase()
            val currency = exchangeRates.first().exchageRate.convertToDouble()

            Log.d(TAG, "exchangeRates: $exchangeRates / currency = $currency")

            reduceState {
                copy(
                    exchangeRates = exchangeRates,
                    currency = currency,
                )
            }
        }
    }

    /**
     * 볼린저 매니저 초기화
     * 구독 중인 종목들의 20분봉 데이터를 서버에서 가져와 MinuteAggregator 초기화
     */
    private fun initBollingerManager() {
        launch(Dispatchers.IO) {
            try {
                val subscribedStocks = roomDatabaseRepository.getAllSubscribedStocks().first()
                val codes = subscribedStocks.map { it.code }

                Logger.d("Initializing BollingerManager with ${codes.size} stocks: $codes")

                bollingerManager.initializeStocks(codes)

                reduceState {
                    copy(isBollingerInitialized = true)
                }

                Logger.d("BollingerManager initialized successfully")
            } catch (e: Exception) {
                Logger.e("Failed to initialize BollingerManager: ${e.message}")           }
        }
    }

    /**
     * 새 종목 추가 시 볼린저 매니저에도 추가
     */
    fun addStockToBollinger(code: String) {
        launch(Dispatchers.IO) {
            bollingerManager.addStock(code)
        }
    }

    /**
     * 종목 제거 시 볼린저 매니저에서도 제거
     */
    fun removeStockFromBollinger(code: String) {
        launch(Dispatchers.IO) {
            bollingerManager.removeStock(code)
        }
    }

    /**
     * 특정 종목의 볼린저 알림 상태 초기화
     */
    fun clearBollingerAlert(code: String) {
        bollingerManager.clearAlert(code)
    }

    /**
     * 모든 볼린저 알림 상태 초기화
     */
    fun clearAllBollingerAlerts() {
        bollingerManager.clearAllAlerts()
    }

    /**
     *  ViewModel 리소스 해제
     */
    override fun onCleared() {
        super.onCleared()
        stockTickJob.cancel()
        bollingerTickJob.cancel()
        bollingerAlertJob.cancel()
    }
}


data class SubscribeState(
    val tick: List<NasdaqUiData> = emptyList(),
    val exchangeRates: List<ExchangeRateData> = emptyList(),
    val currency: Double = 0.0,
    /** 볼린저 하한가 달성 종목 코드 리스트 */
    val bollingerLowerAlertCodes: List<String> = emptyList(),
    /** 볼린저 초기화 완료 여부 */
    val isBollingerInitialized: Boolean = false,
)

sealed interface SubscribeSideEffect {
    data class TokenErrorPopup(val description: String?, val code: String?) : MainSideEffect
    data class NetworkErrorToast(val description: String) : MainSideEffect
    data class NetworkSuccessToast(val description: String) : MainSideEffect

    /** 볼린저 하한가 달성 알림 */
    data class BollingerLowerAlert(val code: String, val name: String): MainSideEffect
}