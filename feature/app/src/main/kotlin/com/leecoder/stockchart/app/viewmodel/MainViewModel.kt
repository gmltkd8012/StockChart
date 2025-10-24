package com.leecoder.stockchart.app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leecoder.data.token.TokenRepository
import com.leecoder.network.util.NetworkResult
import com.leecoder.stockchart.model.token.TokenError
import com.leecoder.stockchart.ui.base.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
): StateViewModel<MainState, MainSideEffect>(MainState()) {

    internal fun postToken() {
        launch(Dispatchers.IO) {
            val post = tokenRepository.postToken(
                "client_credentials",
                "",
                "")

            if (!post.first) showErrorPopup(post.second)
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
}

data class MainState(
    val krInvestTokenExpired: String? = null,
)

sealed interface MainSideEffect {
    data class TokenErrorPopup(val description: String?, val code: String?): MainSideEffect
}