package com.leecoder.stockchart.app.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.leecoder.data.repository.KsInvestmentRepository
import com.leecoder.data.repository.RegistedStockRepository
import com.leecoder.data.repository.RoomDatabaseRepository
import com.leecoder.data.repository.WebSocketRepository
import com.leecoder.data.token.TokenRepository
import com.leecoder.network.const.Credential
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.domain.usecase.SaveAllBollingersUseCase
import com.leecoder.stockchart.domain.usecase.UpdateCurrentPricesUseCase
import com.leecoder.stockchart.model.network.WebSocketState
import com.leecoder.stockchart.model.token.TokenError
import com.leecoder.stockchart.ui.base.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.WebSocket
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val webSocketRepository: WebSocketRepository,
    private val tokenRepository: TokenRepository,
    private val ksInvestmentRepository: KsInvestmentRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val roomDatabaseRepository: RoomDatabaseRepository,
    private val registedStockRepository: RegistedStockRepository,
    private val saveAllBollingersUseCase: SaveAllBollingersUseCase,
    private val updateCurrentPricesUseCase: UpdateCurrentPricesUseCase,
): StateViewModel<SplashState, SplashSideEffect>(SplashState()) {

    internal fun checkToken(){
        launch(Dispatchers.IO) {
            val tokenExpiredTime = dataStoreRepository.currentKrInvestmentTokenExpired.first() ?: 0L
            val storeToken = dataStoreRepository.currentKrInvestmentToken.first()

            if (tokenExpiredTime < System.currentTimeMillis() || storeToken == null) {
                tokenRepository.postToken(
                    Credential.CLIENT_CREDENTIAL,
                    Credential.APP_SECRET,
                    Credential.APP_KEY,
                ).let { (isSuccess, errorMessage) ->
                    if (!isSuccess) {
                        showErrorPopup(
                            errorCode = errorMessage?.errorCode,
                            errorMessage = errorMessage?.errorDescription
                        )
                        return@launch
                    }
                }
            }

            reduceState {
                copy(hasToken = true)
            }
        }
    }

    internal fun checkAprovalKey() {
        launch(Dispatchers.IO) {
            val approvalKey = dataStoreRepository.currentKrInvestmentWebSocket.first()

            if (approvalKey == null) {
                webSocketRepository.postWebSocket(
                    Credential.CLIENT_CREDENTIAL,
                    Credential.APP_KEY,
                    Credential.APP_SECRET,
                ).let { (isSuccess, errorMessage) ->
                    if (!isSuccess) {
                        showErrorPopup(
                            errorCode = errorMessage?.errorCode,
                            errorMessage = errorMessage?.errorDescription
                        )
                        return@launch
                    }
                }
            }

            reduceState {
                copy(hasApprovalKey = true)
            }
        }
    }

    internal fun connectWebSocket() {
        launch(Dispatchers.IO) {
            webSocketRepository.connect(
                "ws://ops.koreainvestment.com:21000/tryitout/H0STCNT0",
            )

            webSocketRepository.connectedWebSocketSession
                .onEach { state ->
                    if (state is WebSocketState.Error) {
                        showErrorPopup(
                            errorCode = "웹소켓 연결 실패",
                            errorMessage = state.message
                        )
                    }

                    reduceState {
                        copy(connectWebSocekt = state)
                    }
                }.launchIn(this@SplashViewModel)
        }
    }

    internal fun calculatorBollingers() {
        launch(Dispatchers.IO) {
            val insertComplete = saveAllBollingersUseCase()

            reduceState {
                copy(isCompleteBollinger = insertComplete)
            }
        }
    }

    internal fun saveCurrentPrices() {
        launch(Dispatchers.IO) {
            updateCurrentPricesUseCase()
        }
    }

    internal fun initSubcribeStock() {
        launch(Dispatchers.IO) {
            val registedStock = registedStockRepository.getRegistedStock().first()

            webSocketRepository.initSubscribe(
                registedStock.map { it.code }
            )
        }
    }

    private fun showErrorPopup(errorCode: String?, errorMessage: String?) {
        launch(Dispatchers.IO) {
            sendSideEffect(SplashSideEffect.ErrorPopup(
                errorCode = errorCode,
                errorMessage = errorMessage,
            ))
        }
    }
}

sealed interface SplashSideEffect {
    data class ErrorPopup(
        val errorCode: String?,
        val errorMessage: String?,
    ): SplashSideEffect
}

data class SplashState(
    val hasToken: Boolean = false,
    val hasApprovalKey: Boolean = false,
    val isCompleteBollinger: Boolean = false,
    val connectWebSocekt: WebSocketState = WebSocketState.Connecting,
)