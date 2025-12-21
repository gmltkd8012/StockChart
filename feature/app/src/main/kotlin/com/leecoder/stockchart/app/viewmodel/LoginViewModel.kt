package com.leecoder.stockchart.app.viewmodel

import com.leecoder.data.token.TokenRepository
import com.leecoder.stockchart.ui.base.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
): StateViewModel<LoginState, Nothing>(LoginState()) {

    internal fun updateAppKeyField(text: String) {
        launch {
            reduceState {
                copy(appKeyField = text)
            }
        }
    }

    internal fun updateAppSecretField(text: String) {
        launch {
            reduceState {
                copy(appSecretField = text)
            }
        }
    }

    internal fun login() {
        launch(Dispatchers.IO) {
            val appKey = state.value.appKeyField
            val appSecret = state.value.appSecretField

            // TODO: 로그인 로직 구현
        }
    }

}

data class LoginState(
    val appKeyField: String = "",
    val appSecretField: String = "",
)