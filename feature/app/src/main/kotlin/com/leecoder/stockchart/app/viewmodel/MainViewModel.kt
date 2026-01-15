package com.leecoder.stockchart.app.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.test.core.app.ActivityScenario.launch
import com.leecoder.data.repository.KsInvestmentRepository
import com.leecoder.data.repository.WebSocketRepository
import com.leecoder.data.repository.room.RoomDatabaseRepository
import com.leecoder.network.util.NetworkStateObserver
import com.leecoder.stockchart.domain.manager.BollingerManager
import com.leecoder.stockchart.domain.usecase.ReconnectWebSocketUseCase
import com.leecoder.stockchart.domain.usecase.overseas.SaveOverseasStockCurrentPriceUseCase
import com.leecoder.stockchart.domain.usecase.search.SearchSymbolUseCase
import com.leecoder.stockchart.model.network.WebSocketState
import com.leecoder.stockchart.model.stock.SubscribedStockData
import com.leecoder.stockchart.model.symbol.SymbolData
import com.leecoder.stockchart.model.ui.BollingerUiData
import com.leecoder.stockchart.model.ui.NasdaqUiData
import com.leecoder.stockchart.ui.base.StateViewModel
import com.leecoder.stockchart.util.calculator.MinuteAggregator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val webSocketRepository: WebSocketRepository,
    private val roomDatabaseRepository: RoomDatabaseRepository,
    private val searchSymbolUseCase: SearchSymbolUseCase,
    private val networkStateObserver: NetworkStateObserver,
    private val reconnectWebSocketUseCase: ReconnectWebSocketUseCase,
    private val saveOverseasStockCurrentPriceUseCase: SaveOverseasStockCurrentPriceUseCase,
    private val bollingerManager: BollingerManager,
): StateViewModel<MainState, MainSideEffect>(MainState()) {

    private val _subscribedMap = mutableStateMapOf<String, NasdaqUiData>()
    private val _bollingerLowersMap = mutableStateMapOf<String, BollingerUiData>()
    private val _subscribedLiveBollingerMap = mutableStateMapOf<String, MinuteAggregator>()

    private val _textFieldState = MutableStateFlow<String>("")
    val textFieldState: StateFlow<String> = _textFieldState

    @OptIn(FlowPreview::class)
    private val debouncedSearchQuery: Flow<String> = _textFieldState
        .map { it }
        .distinctUntilChanged()
        .debounce(200)
        .shareIn(this@MainViewModel, SharingStarted.Lazily, replay = 1)

    init {
        debouncedSearchQuery
            .onEach { query ->
                if (query.isBlank()) {
                    reduceState {
                        copy(searchResults = emptyList())
                    }
                } else {
                    val result = searchSymbolUseCase("NAS", query).first() // 현재 버전 나스닥 고정
                    reduceState {
                        copy(searchResults = result)
                    }
                }
            }.launchIn(this@MainViewModel)

        observeNetworkState()
    }

    private fun observeNetworkState() {
        launch(Dispatchers.IO) {
            networkStateObserver.isConnectedNetwork
                .collect { isConnected ->
                    Log.i("lynn", "연결 상태 감지 ... -> $isConnected")

                    if (isConnected) {
                        showNetworkSuccessPopup()

                        launch(Dispatchers.IO) {
                            val session = webSocketRepository.connectedWebSocketSession.first()

                            if (session is WebSocketState.Disconnected || session is WebSocketState.Error) {
                                reconnectWebSocketUseCase()
                            }
                        }
                    } else{
                        showNetowrkErrorPopup()
                    }
                }
        }
    }

    fun onQueryChanged(text: String) {
        _textFieldState.value = text
    }

    fun subscribeStock(code: String, name: String) {
        launch(Dispatchers.IO) {
            saveOverseasStockCurrentPriceUseCase(SubscribedStockData(code, name))
            webSocketRepository.subscribe(code)
            // 볼린저 계산을 위해 BollingerManager에 종목 추가
            bollingerManager.addStock(code)
        }
    }

    fun unSubsctibeStock(code: String, name: String) {
        launch(Dispatchers.IO) {
            roomDatabaseRepository.unSubscribeStock(SubscribedStockData(code, name))
            webSocketRepository.unSubscribe(code)
            // 볼린저 매니저에서 종목 제거
            bollingerManager.removeStock(code)
        }
    }

    private fun showNetowrkErrorPopup() {
        launch(Dispatchers.IO) {
            sendSideEffect(
                MainSideEffect.NetworkErrorToast("네트워크 연결을 확인해주세요.")
            )
        }
    }

    private fun showNetworkSuccessPopup() {
        launch(Dispatchers.IO) {
            sendSideEffect(
                MainSideEffect.NetworkSuccessToast("네트워크에 연결 되었어요.")
            )
        }
    }
}

data class MainState(
    val isConnected: Boolean = false,
    val searchResults: List<SymbolData> = emptyList(),
)

sealed interface MainSideEffect {
    data class TokenErrorPopup(val description: String?, val code: String?): MainSideEffect
    data class NetworkErrorToast(val description: String): MainSideEffect
    data class NetworkSuccessToast(val description: String): MainSideEffect
}