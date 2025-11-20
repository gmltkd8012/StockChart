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
import com.leecoder.data.token.TokenRepository
import com.leecoder.stockchart.work.worker.MarketWorker
import com.leecoder.network.const.Credential
import com.leecoder.stockchart.appconfig.config.AppConfig
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.domain.usecase.SaveAllBollingersUseCase
import com.leecoder.stockchart.domain.usecase.SaveStockWithCurrentPriceUseCase
import com.leecoder.stockchart.domain.usecase.exchage.CheckExChangeRateUseCase
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
    private val webSocketRepository: WebSocketRepository,
    private val tokenRepository: TokenRepository,
    private val ksInvestmentRepository: KsInvestmentRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val roomDatabaseRepository: RoomDatabaseRepository,
    private val saveAllBollingersUseCase: SaveAllBollingersUseCase,
    private val saveStockWithCurrentPriceUseCase: SaveStockWithCurrentPriceUseCase,
    private val checkExChangeRateUseCase: CheckExChangeRateUseCase,
    private val saveOverseasStockCurrentPriceUseCase: SaveOverseasStockCurrentPriceUseCase,
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
            webSocketRepository.connect(appConfig.webSocketUrl + appConfig.kospiUrl)

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

    internal fun checkCurrentExchangeRate() {
        launch(Dispatchers.IO) {
            checkExChangeRateUseCase()
        }
    }

    internal fun startMarketInfoWorker() {
        launch(Dispatchers.IO) {
            val (targetOption, targetDelay) = ScheduleUtil.caculatorDelayMillisToNextTarget()
            Log.e("[Leecoder]", "Worker Request -> $targetDelay")

            val input = workDataOf(
                MarketWorker.INPUT_DATA_TARGET_OPTION to targetOption
            )

            val request = OneTimeWorkRequestBuilder<MarketWorker>()
                .setInputData(input)
                .setInitialDelay(targetDelay, TimeUnit.MILLISECONDS)
                .build()

            workManager.enqueueUniqueWork(
                MarketWorker.Companion.UNIQUE_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                request
            )

            workManager.getWorkInfoByIdLiveData(request.id).asFlow()
                .collect() { wi ->
                    when (wi.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            workManager.enqueueUniqueWork(
                                MarketWorker.UNIQUE_WORK_NAME,
                                ExistingWorkPolicy.REPLACE,
                                request
                            )
                        }
                        else -> {
                            //TODO - 에러케이스
                        }
                    }
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
}

data class SplashState(
    val hasToken: Boolean = false,
    val hasApprovalKey: Boolean = false,
    val hasMarketInfo: Boolean = false,
    val isCompleteBollinger: Boolean = false,
    val connectWebSocekt: WebSocketState = WebSocketState.Disconnected,
)