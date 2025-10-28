package com.leecoder.stockchart.app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leecoder.data.repository.WebSocketRepository
import com.leecoder.data.token.TokenRepository
import com.leecoder.network.const.Credential
import com.leecoder.network.util.NetworkResult
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.model.token.TokenError
import com.leecoder.stockchart.ui.base.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
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
): StateViewModel<MainState, MainSideEffect>(MainState()) {

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

            connectToWebSocket()
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
    val krInvestTokenExpired: String? = null,
)

sealed interface MainSideEffect {
    data class TokenErrorPopup(val description: String?, val code: String?): MainSideEffect
}