package com.leecoder.stockchart.app.viewmodel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leecoder.data.repository.KsInvestmentRepository
import com.leecoder.data.repository.RoomDatabaseRepository
import com.leecoder.data.repository.WebSocketRepository
import com.leecoder.data.token.TokenRepository
import com.leecoder.network.const.Credential
import com.leecoder.network.util.NetworkResult
import com.leecoder.stockchart.datastore.const.DataStoreConst
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.domain.usecase.AddLiveBollingersUseCase
import com.leecoder.stockchart.domain.usecase.InitLiveBollingersUseCase
import com.leecoder.stockchart.domain.usecase.SaveStockWithCurrentPriceUseCase
import com.leecoder.stockchart.domain.usecase.SearchKrxSymbolUseCase
import com.leecoder.stockchart.model.bollinger.LiveBollingerData
import com.leecoder.stockchart.model.room.BollingerData
import com.leecoder.stockchart.model.stock.StockTick
import com.leecoder.stockchart.model.stock.SubscribedStockData
import com.leecoder.stockchart.model.symbol.KrxSymbolData
import com.leecoder.stockchart.model.token.TokenError
import com.leecoder.stockchart.model.ui.StockUiData
import com.leecoder.stockchart.ui.base.StateViewModel
import com.leecoder.stockchart.util.calculator.BollingerAggregator
import com.leecoder.stockchart.util.calculator.BollingerCalculator
import com.leecoder.stockchart.util.calculator.MinuteAggregator
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
    private val ksInvestmentRepository: KsInvestmentRepository,
    private val roomDatabaseRepository: RoomDatabaseRepository,
    private val searchKrxSymbolUseCase: SearchKrxSymbolUseCase,
    private val initLiveBollingersUseCase: InitLiveBollingersUseCase,
    private val addLiveBollingersUseCase: AddLiveBollingersUseCase,
    private val saveStockWithCurrentPriceUseCase: SaveStockWithCurrentPriceUseCase,
): StateViewModel<MainState, Nothing>(MainState()) {

    private val _subscribedMap = mutableStateMapOf<String, StockUiData>()
    private val _bollingerLowers = mutableSetOf<BollingerData>()
    private val _subscribedLiveBollingerMap = mutableStateMapOf<String, MinuteAggregator>()

    private var curBollingerSetting = ""

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

           roomDatabaseRepository.getAllSubscribedStocks()
                .flatMapLatest { subscribedStocks ->
                    if (subscribedStocks.isEmpty()) {
                        reduceState {
                            copy(
                                stockTickMap = emptyMap()
                            )
                        }
                        return@flatMapLatest emptyFlow()
                    }

                    val bollingerLowerList = roomDatabaseRepository.getAllBollingers().first()

                    val codeToName = subscribedStocks.associate { it.code to it.name }

                    val removeList = _subscribedMap.keys.filter { it !in codeToName.keys }
                    removeList.forEach { key ->
                        _subscribedMap.remove(key)
                    }

                    val addList = codeToName.keys.filter { it !in _subscribedMap.keys }
                    addList.forEach { key ->
                        _subscribedMap[key] = StockUiData()
                        _subscribedLiveBollingerMap.putAll(addLiveBollingersUseCase(key))
                    }

                    stockTickFlow
                        .filter { it.mkscShrnIscd in _subscribedMap.keys }
                        .onEach { tick ->


                            if (curBollingerSetting == DataStoreConst.ValueConst.BOLLINGER_LIVE_SETTING) {
                                checkLiveBollinger(tick)
                            } else {
                                //TODO - 데일리 볼린저 로직
                            }

                          //  val currentBollingerData = bollingerLowerList.find { it.code == tick.mkscShrnIscd }
//                            currentBollingerData?.let { v -> // 종목 데이터 틱당 볼린저 계산하여 리스트 등록
//                                val currentPrice = tick.stckPrpr?.toInt() ?: 0
//
//                                if (currentBollingerData.lower > currentPrice && currentPrice > 0) {
//                                    _bollingerLowers.add(v)
//                                }
//                            }

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
                                    stockTickMap = _subscribedMap.toMap(),
                                    bollingerLowers = _bollingerLowers.toList()
                                )
                            }
                        }
                }.launchIn(viewModelScope)
        }
    }

    fun subscribeStock(code: String, name: String) {
        launch(Dispatchers.IO) {
            Log.i("lynn", "[MainViewModel] -> ${this@launch.coroutineContext}")
            saveStockWithCurrentPriceUseCase(SubscribedStockData(code, name))
            webSocketRepository.subscribe(code)
        }
    }

    fun unSubsctibeStock(code: String, name: String) {
        launch(Dispatchers.IO) {
            roomDatabaseRepository.unSubscribeStock(SubscribedStockData(code, name))
            webSocketRepository.unSubscribe(code)
        }
    }

    internal fun initBollingerData() {
        // TODO - 데이터 스토어를 통한 분기 처리 적용
        launch(Dispatchers.IO) {
            dataStoreRepository.currentBollingerSetting.collect { currentBollingerSetting ->
                val subscribeStocks = roomDatabaseRepository.getAllSubscribedStocks().first().map { it.code }

                if (currentBollingerSetting == DataStoreConst.ValueConst.BOLLINGER_LIVE_SETTING) {
                    // 실시간 데이터 Map 저장
                    Log.e("lynn", "실시간 볼린저 설정")
                    if (_subscribedLiveBollingerMap.isNotEmpty()) _subscribedLiveBollingerMap.clear()
                    _subscribedLiveBollingerMap.putAll(initLiveBollingersUseCase(subscribeStocks))

                } else {
                    Log.d("lynn", "데일리 볼린저 설정")

                }

                curBollingerSetting = currentBollingerSetting
            }
        }
    }


    private suspend fun checkLiveBollinger(tick: StockTick) {
        val agg = _subscribedLiveBollingerMap[tick.mkscShrnIscd]
        val result = agg?.onTick(tick.stckCntgHour ?: "", tick.stckPrpr?.toIntOrNull() ?: 0)

        result?.let {
            val currentPrice = tick.stckPrpr?.toInt() ?: 0
            val bollingerData = it.bollinger

            bollingerData?.let { v ->
                val lowerPrice = v.lower

                if (lowerPrice > currentPrice && currentPrice > 0) {
                    _bollingerLowers.add(v)
                }
            }
        }

        Log.i("lynn", "result -> $result")
    }

    private suspend fun checkDailyBollinger(tick: StockTick) {

    }
}

data class MainState(
    val isConnected: Boolean = false,
    val krInvestTokenExpired: String? = null,
    val stockTickMap: Map<String, StockUiData>? = null,
    val searchResultList: List<KrxSymbolData>? = null,
    val bollingerLowers: List<BollingerData> = emptyList()
)

sealed interface MainSideEffect {
    data class TokenErrorPopup(val description: String?, val code: String?): MainSideEffect
}