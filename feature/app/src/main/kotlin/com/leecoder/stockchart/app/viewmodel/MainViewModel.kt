package com.leecoder.stockchart.app.viewmodel

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.net.InetAddress
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val webSocketRepository: WebSocketRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val registedStockRepository: RegistedStockRepository,
    private val searchKrxSymbolUseCase: SearchKrxSymbolUseCase,
): StateViewModel<MainState, MainSideEffect>(MainState()) {

    private val tickMap = mutableMapOf<String, StockUiData>()

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

        webSocketRepository.connectedWebSocketSession
            .onEach { isConnected ->
                reduceState {
                    copy(isConnected = isConnected)
                }
            }.launchIn(viewModelScope)
    }

    fun onQueryChanged(text: String) {
        _textFieldState.value = text
    }

    internal fun checkExpiredToken() {
        launch(Dispatchers.IO) {
            val tokenExpiredTime =
                dataStoreRepository.currentKrInvestmentTokenExpired.first() ?: 0L

            launch(Dispatchers.IO) {
                if (tokenExpiredTime < System.currentTimeMillis()) {
                    val post = tokenRepository.postToken(
                        Credential.CLIENT_CREDENTIAL,
                        Credential.APP_SECRET,
                        Credential.APP_KEY,
                    )

                    if (!post.first) showErrorPopup(post.second)
                }
            }

            launch(Dispatchers.IO) {
                if (dataStoreRepository.currentKrInvestmentWebSocket.first() == null) {
                    val post = webSocketRepository.postWebSocket(
                        Credential.CLIENT_CREDENTIAL,
                        Credential.APP_KEY,
                        Credential.APP_SECRET,
                    )

                    if (!post.first) showErrorPopup(post.second)
                }
            }


        }
    }

    internal fun connectWebSocket() {
        connectToWebSocket()
        collectStockTick()
    }

    private fun collectStockTick() {
        viewModelScope.launch(Dispatchers.IO) {
            webSocketRepository.channelStockTick.consumeEach { tick ->
                Log.d("heesang", "collectStockTick: $tick")

                tick.mkscShrnIscd?.let { iscd ->
                    val parseName = registedStockRepository.getRegistedStockByCode(iscd).name

                    tickMap[iscd] = StockUiData(
                        code = iscd,
                        name = parseName,
                        tradePrice = tick.stckPrpr?.toInt(),
                        priceDiff = tick.prdyVrss?.toInt(),
                    )
                }


                reduceState {
                    copy(stockTickMap = tickMap.toMap())
                }
            }
        }
    }

    fun initSubcribeStock() {
        launch(Dispatchers.IO) {
            val registedStock = registedStockRepository.getRegistedStock().first()
            webSocketRepository.initSubscribe(registedStock.map { it.code })

            reduceState {
                copy(registedStock = registedStock.size)
            }
        }
    }

    fun addSubscribeStock(code: String, name: String) {
        launch(Dispatchers.IO) {
            registedStockRepository.insert(RegistedStockData(code, name))
            val registedStock = registedStockRepository.getRegistedStock().first()

            reduceState {
                copy(registedStock = registedStock.size)
            }

            webSocketRepository.addSubscribe(code)
        }
    }

    private fun showErrorPopup(error: TokenError?) {
        launch(Dispatchers.IO) {
            sendSideEffect(MainSideEffect.TokenErrorPopup(
                description = error?.errorDescription,
                code = error?.errorCode,
            ))
        }
    }


    private fun connectToWebSocket() {
        webSocketRepository.connect(
            "ws://ops.koreainvestment.com:21000/tryitout/H0STCNT0",
        )
    }
}

data class MainState(
    val isConnected: Boolean = false,
    val krInvestTokenExpired: String? = null,
    val stockTickMap: Map<String, StockUiData>? = null,
    val searchResultList: List<KrxSymbolData>? = null,
    val registedStock: Int = 0
)

sealed interface MainSideEffect {
    data class TokenErrorPopup(val description: String?, val code: String?): MainSideEffect
}