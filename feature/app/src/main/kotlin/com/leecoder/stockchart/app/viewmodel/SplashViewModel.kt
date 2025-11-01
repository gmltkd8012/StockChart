package com.leecoder.stockchart.app.viewmodel

import com.leecoder.data.repository.KsInvestmentRepository
import com.leecoder.data.repository.WebSocketRepository
import com.leecoder.data.token.TokenRepository
import com.leecoder.network.const.Credential
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.model.token.TokenError
import com.leecoder.stockchart.ui.base.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val webSocketRepository: WebSocketRepository,
    private val tokenRepository: TokenRepository,
    private val ksInvestmentRepository: KsInvestmentRepository,
    private val dataStoreRepository: DataStoreRepository,
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
                        showTokenErrorPopup(errorMessage)
                        return@launch
                    }
                }
            }

            reduceState {
                copy(hasToken = true)
            }
        }
    }

    private fun showTokenErrorPopup(error: TokenError?) {
        launch(Dispatchers.IO) {
            sendSideEffect(SplashSideEffect.ErrorPopup(
                errorCode = error?.errorCode,
                errorMessage = error?.errorDescription,
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
    val connectWebSocekt: Boolean = false,
)