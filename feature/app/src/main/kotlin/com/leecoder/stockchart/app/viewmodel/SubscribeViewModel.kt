package com.leecoder.stockchart.app.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.viewModelScope
import com.leecoder.data.repository.WebSocketRepository
import com.leecoder.data.repository.room.RoomDatabaseRepository
import com.leecoder.data.repository.stock.StockRepository
import com.leecoder.data.token.TokenRepository
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.domain.usecase.exchage.GetExchangeRateUsecase
import com.leecoder.stockchart.model.exchange.ExchangeRateData
import com.leecoder.stockchart.model.symbol.SymbolData
import com.leecoder.stockchart.model.ui.BollingerUiData
import com.leecoder.stockchart.model.ui.NasdaqUiData
import com.leecoder.stockchart.ui.base.StateViewModel
import com.leecoder.stockchart.util.extension.convertToDouble
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscribeViewModel @Inject constructor(
    private val stockRepository: StockRepository,
    private val getExchangeRateUsecase: GetExchangeRateUsecase,
): StateViewModel<SubscribeState, SubscribeSideEffect>(SubscribeState()) {

    // 실시간 WebSocket 데이터 구독
    private val stockTickJob: Job =
        stockRepository.stocksUiFlow
            .onEach { tick ->
                Log.i("heesang", "[tick]: $tick")
                reduceState {
                    copy(tick = tick)
                }
            }.launchIn(viewModelScope)

    /**
     *  ViewModel 초기화 작업
     */
    init {
        initExchangeRate()
    }

    // 환율 조회
    private fun initExchangeRate() {
        launch {
            val exchangeRates = getExchangeRateUsecase()
            val currency = exchangeRates.first().exchageRate.convertToDouble()

            Log.d(
                "heesang", "exchangeRates: ${exchangeRates} / currency = ${currency} ")

            reduceState {
                copy(
                    exchangeRates = exchangeRates,
                    currency = currency,
                )
            }
        }
    }


    /**
     *  ViewModel 리소스 해제
     */
    override fun onCleared() {
        super.onCleared()
        stockTickJob.cancel()
    }
}


data class SubscribeState(
    val tick: List<NasdaqUiData> = emptyList(),
    val exchangeRates: List<ExchangeRateData> = emptyList(),
    val currency: Double = 0.0,
)

sealed interface SubscribeSideEffect {
    data class TokenErrorPopup(val description: String?, val code: String?): MainSideEffect
    data class NetworkErrorToast(val description: String): MainSideEffect
    data class NetworkSuccessToast(val description: String): MainSideEffect
}