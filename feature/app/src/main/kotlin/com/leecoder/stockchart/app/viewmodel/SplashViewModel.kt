package com.leecoder.stockchart.app.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.asFlow
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.leecoder.data.repository.KsInvestmentRepository
import com.leecoder.data.repository.room.RoomDatabaseRepository
import com.leecoder.data.repository.WebSocketRepository
import com.leecoder.data.token.AuthRepository
import com.leecoder.network.const.Credential
import com.leecoder.stockchart.appconfig.config.AppConfig
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.domain.usecase.SaveStockWithCurrentPriceUseCase
import com.leecoder.stockchart.domain.usecase.overseas.SaveOverseasStockCurrentPriceUseCase
import com.leecoder.stockchart.model.network.WebSocketState
import com.leecoder.stockchart.ui.base.StateViewModel
import com.leecoder.stockchart.util.time.ScheduleUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val workManager: WorkManager,
    private val appConfig: AppConfig,
    private val authRepository: AuthRepository,
    private val webSocketRepository: WebSocketRepository,
    private val ksInvestmentRepository: KsInvestmentRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val roomDatabaseRepository: RoomDatabaseRepository,
    private val saveStockWithCurrentPriceUseCase: SaveStockWithCurrentPriceUseCase,
    private val saveOverseasStockCurrentPriceUseCase: SaveOverseasStockCurrentPriceUseCase,
): StateViewModel<SplashState, SplashSideEffect>(SplashState()) {

    internal fun checkLoginStatus() {
        launch(Dispatchers.IO) {
            val appKey = dataStoreRepository.currentAppKey.first()
            val appSecret = dataStoreRepository.currentAppSecret.first()

            if (appKey.isNullOrEmpty() || appSecret.isNullOrEmpty()) {
                sendSideEffect(SplashSideEffect.NavigateToLogin)
            } else {
                reduceState {
                    copy(isLoggedIn = true)
                }
            }
        }
    }

    internal fun checkToken() {
        launch(Dispatchers.IO) {
            val appKey = dataStoreRepository.currentAppKey.first() ?: return@launch
            val appSecret = dataStoreRepository.currentAppSecret.first() ?: return@launch
            val tokenExpiredTime = dataStoreRepository.currentKrInvestmentTokenExpired.first() ?: 0L

            if (tokenExpiredTime < System.currentTimeMillis()) {
                authRepository.postToken(
                    Credential.CLIENT_CREDENTIAL,
                    appSecret,
                    appKey,
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
            val appKey = dataStoreRepository.currentAppKey.first() ?: return@launch
            val appSecret = dataStoreRepository.currentAppSecret.first() ?: return@launch
            val approvalKey = dataStoreRepository.currentKrInvestmentWebSocket.first()

            if (approvalKey == null) {
                webSocketRepository.postWebSocket(
                    Credential.CLIENT_CREDENTIAL,
                    appKey,
                    appSecret,
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
            webSocketRepository.connect()

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

    internal fun initSubcribeStock() {
        launch(Dispatchers.IO) {
            val subscribedStocks = roomDatabaseRepository.getAllSubscribedStocks().first()
            saveStockWithCurrentPriceUseCase(*subscribedStocks.toTypedArray()) // 서버로부터 DB 저장 목록에 대해 현재가 저장
            webSocketRepository.initSubscribe(subscribedStocks.map { it.code }) // DB 저장 목록으로 WebSocket 세션 연결.
        }
    }

    internal fun saveCurrentMarketInfo() {
        launch(Dispatchers.IO) {
            val targetOption = ScheduleUtil.currentTargetOption()
            dataStoreRepository.updateMarketInfo(targetOption)

            reduceState {
                copy(
                    hasMarketInfo = true
                )
            }
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

    data object NavigateToLogin : SplashSideEffect
}

data class SplashState(
    val isLoggedIn: Boolean = false,
    val hasToken: Boolean = false,
    val hasApprovalKey: Boolean = false,
    val hasMarketInfo: Boolean = false,
    val connectWebSocekt: WebSocketState = WebSocketState.Disconnected,
)