package com.leecoder.stockchart.app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leecoder.data.token.TokenRepository
import com.leecoder.network.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
): ViewModel() {

    internal fun postToken() {
        viewModelScope.launch(Dispatchers.IO) {
            val post = tokenRepository.postToken(
                "client_credentials",
                "",
                "")

            Log.d("heesang", "[Token] -> \n${post}")
        }
    }
}