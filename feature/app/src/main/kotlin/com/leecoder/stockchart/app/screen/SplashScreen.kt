package com.leecoder.stockchart.app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leecoder.stockchart.app.viewmodel.MainSideEffect
import com.leecoder.stockchart.app.viewmodel.SplashSideEffect
import com.leecoder.stockchart.app.viewmodel.SplashViewModel
import com.leecoder.stockchart.design_system.component.BaseDialog
import com.leecoder.stockchart.model.network.WebSocketState
import com.leecoder.stockchart.ui.extension.hide
import com.leecoder.stockchart.ui.extension.isShown
import com.leecoder.stockchart.ui.extension.rememberErrorDialogState
import com.leecoder.stockchart.ui.extension.show
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    callMainScreen: () -> Unit,
    onErrorFinish: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var errorDialogState = rememberErrorDialogState<SplashSideEffect.ErrorPopup>()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SplashSideEffect.ErrorPopup -> {
                errorDialogState.show(
                    description = sideEffect.errorMessage,
                    code = sideEffect.errorCode
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.checkToken()
        viewModel.checkAprovalKey()
        viewModel.connectWebSocket()
    }

    LaunchedEffect(state.hasApprovalKey, state.connectWebSocekt) {
        if (state.hasApprovalKey && state.connectWebSocekt is WebSocketState.Connected) {
            viewModel.initSubcribeStock()
        }
    }

    LaunchedEffect(state.hasToken) {
        if (state.hasToken) {
            viewModel.calculatorBollingers()
            viewModel.saveCurrentPrices()
        }
    }

    LaunchedEffect(state.hasToken, state.connectWebSocekt, state.isCompleteBollinger) {
        if (state.hasToken &&
            state.isCompleteBollinger &&
            state.connectWebSocekt is WebSocketState.Connected) {
            callMainScreen()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Gray),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "필요한 데이터 가져오는 중...",
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = Color.White
        )
    }

    if (errorDialogState.isShown) {
        BaseDialog(
            description = errorDialogState.value.description,
            code = errorDialogState.value.code,
            onClickConfirm = {
                errorDialogState.hide()
                onErrorFinish()
            }
        )
    }
}