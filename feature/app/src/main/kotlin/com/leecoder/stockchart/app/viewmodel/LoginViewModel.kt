package com.leecoder.stockchart.app.viewmodel

import com.leecoder.data.token.TokenRepository
import com.leecoder.stockchart.ui.base.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
): StateViewModel<LoginState, Nothing>(LoginState()) {

    internal fun updateTextField(text: String) {
        launch {
            reduceState {
                copy(textField = text)
            }
        }
    }

    internal fun login() {}

}

data class LoginState(
    val textField: String = "",
)