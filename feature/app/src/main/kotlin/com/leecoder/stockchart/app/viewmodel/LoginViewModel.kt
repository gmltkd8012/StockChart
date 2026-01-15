package com.leecoder.stockchart.app.viewmodel

import com.leecoder.data.token.AuthRepository
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.ui.base.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
): StateViewModel<LoginState, LoginSideEffect>(LoginState()) {

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

    internal fun cleanFields() {
        launch {
            reduceState {
                copy(appKeyField = "", appSecretField = "")
            }
        }
    }

    internal fun login() {
        launch(Dispatchers.IO) {
            val appKey = state.value.appKeyField
            val appSecret = state.value.appSecretField

            if (appKey.isBlank() || appSecret.isBlank()) return@launch

            val result = authRepository.checkLogin(appKey, appSecret).first()

            withContext(Dispatchers.Main) {
                if (result)
                    sendSideEffect(LoginSideEffect.Success)
                else
                    sendSideEffect(LoginSideEffect.Failure)
            }
        }
    }

}

data class LoginState(
    val appKeyField: String = "",
    val appSecretField: String = "",
    val isSuccessed: Boolean = false,
)

sealed interface LoginSideEffect {
    data object Success: LoginSideEffect
    data object Failure: LoginSideEffect
}