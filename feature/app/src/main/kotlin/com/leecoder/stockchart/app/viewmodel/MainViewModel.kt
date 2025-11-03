package com.leecoder.stockchart.app.viewmodel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leecoder.data.repository.KsInvestmentRepository
import com.leecoder.data.repository.RegistedStockRepository
import com.leecoder.data.repository.WebSocketRepository
import com.leecoder.data.token.TokenRepository
import com.leecoder.network.const.Credential
import com.leecoder.network.util.NetworkResult
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.domain.usecase.SearchKrxSymbolUseCase
import com.leecoder.stockchart.model.stock.RegistedStockData
import com.leecoder.stockchart.model.stock.StockTick
import com.leecoder.stockchart.model.symbol.KrxSymbolData
import com.leecoder.stockchart.model.token.TokenError
import com.leecoder.stockchart.model.ui.StockUiData
import com.leecoder.stockchart.ui.base.StateViewModel
import com.leecoder.stockchart.util.calculator.BollingerCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.net.InetAddress
import java.nio.file.Files.find
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val webSocketRepository: WebSocketRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val registedStockRepository: RegistedStockRepository,
    private val ksInvestmentRepository: KsInvestmentRepository,
    private val searchKrxSymbolUseCase: SearchKrxSymbolUseCase,
): StateViewModel<MainState, Nothing>(MainState()) {

    private val _subscribedMap = mutableStateMapOf<String, StockUiData>()

    private val _textFieldState = MutableStateFlow<String>("")
    val textFieldState: StateFlow<String> = _textFieldState

    @OptIn(FlowPreview::class)
    private val debouncedSearchQuery: Flow<String> = _textFieldState
        .map { it }
        .distinctUntilChanged()
        .debounce(200)
        .shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)

    init {
        debouncedSearchQuery
            .onEach { query ->
                if (query.isBlank()) {
                    reduceState {
                        copy(searchResultList = emptyList())
                    }
                } else {
                    val result = searchKrxSymbolUseCase(query).first()
                    reduceState {
                        copy(searchResultList = result)
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun onQueryChanged(text: String) {
        _textFieldState.value = text
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    internal fun collectStockTick() {
        viewModelScope.launch(Dispatchers.IO) {
            val stockTickFlow = webSocketRepository.channelStockTick
                .consumeAsFlow()
                .shareIn(viewModelScope, SharingStarted.Eagerly, replay = 0)

           registedStockRepository.getRegistedStock()
                .flatMapLatest { subscribedStocks ->
                    if (subscribedStocks.isEmpty()) {
                        reduceState {
                            copy(
                                stockTickMap = emptyMap()
                            )
                        }
                        return@flatMapLatest emptyFlow()
                    }

                    val codeToName = subscribedStocks.associate { it.code to it.name }

                    val removeList = _subscribedMap.keys.filter { it !in codeToName.keys }
                    removeList.forEach { key ->
                        _subscribedMap.remove(key)
                    }

                    val addList = codeToName.keys.filter { it !in _subscribedMap.keys }
                    addList.forEach { key ->
                        _subscribedMap[key] = StockUiData()
                    }

                    stockTickFlow
                        .filter { it.mkscShrnIscd in _subscribedMap.keys }
                        .onEach { tick ->
                            val stockUiData = StockUiData(
                                code = tick.mkscShrnIscd,
                                name = codeToName[tick.mkscShrnIscd],
                                tradePrice = tick.stckPrpr?.toInt(),
                                priceDiff = tick.prdyVrss?.toInt(),
                            )

                            _subscribedMap.put(
                                key = tick.mkscShrnIscd ?: "",
                                value = stockUiData
                            )

                            reduceState {
                                copy(
                                    stockTickMap = _subscribedMap.toMap()
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }
    }

    fun subscribeStock(code: String, name: String) {
        launch(Dispatchers.IO) {
            registedStockRepository.insert(RegistedStockData(code, name))
            webSocketRepository.subscribe(code)
        }
    }

    fun unSubsctibeStock(code: String, name: String) {
        launch(Dispatchers.IO) {
            registedStockRepository.delete(RegistedStockData(code, name))
            webSocketRepository.unSubscribe(code)
        }
    }
}

data class MainState(
    val isConnected: Boolean = false,
    val krInvestTokenExpired: String? = null,
    val stockTickMap: Map<String, StockUiData>? = null,
    val searchResultList: List<KrxSymbolData>? = null,
    val bollingerLowers: List<RegistedStockData> = emptyList()
)

sealed interface MainSideEffect {
    data class TokenErrorPopup(val description: String?, val code: String?): MainSideEffect
}