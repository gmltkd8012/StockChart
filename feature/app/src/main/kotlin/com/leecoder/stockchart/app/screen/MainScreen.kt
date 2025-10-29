package com.leecoder.stockchart.app.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.leecoder.stockchart.app.viewmodel.MainSideEffect
import com.leecoder.stockchart.app.viewmodel.MainViewModel
import com.leecoder.stockchart.design_system.component.BaseDialog
import com.leecoder.stockchart.ui.extension.hide
import com.leecoder.stockchart.ui.extension.isShown
import com.leecoder.stockchart.ui.extension.rememberErrorDialogState
import com.leecoder.stockchart.ui.extension.show

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onFinish: () -> Unit,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    var errorDialogState = rememberErrorDialogState<MainSideEffect.TokenErrorPopup>()
    val stockTickList = state.stockTickMap?.values?.toList() ?: emptyList()

    LaunchedEffect(Unit) {
        viewModel.connectWebSocket()
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MainSideEffect.TokenErrorPopup -> {
                errorDialogState.show(
                    description = sideEffect.description,
                    code = sideEffect.code
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {

            Button (
                onClick = { viewModel.subscribeStock("000660") }
            ) {
                Text(
                    text = "종목 추가",
                    style = TextStyle(fontSize = 30.sp),
                )
            }

            Spacer(Modifier.height(30.dp))

            if (stockTickList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "서버 연결 중...",
                        style = TextStyle(
                            fontSize = 50.sp,
                            color = Color.Gray
                        )
                    )
                }
            } else {
                LazyColumn {
                    items(stockTickList) { stockTick ->

                        Row {
                            Text(
                                text = stockTick.mkscShrnIscd?.iscdName() ?: "UNKNOWN",
                                style = TextStyle(
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            )

                            Spacer(Modifier.width(12.dp))

                            Text(
                                text = stockTick.stckPrpr + " (${stockTick.prdyVrss})",
                                style = TextStyle(
                                    fontSize = 30.sp,
                                    color = when {
                                        (stockTick.prdyVrss?.toInt() ?: 0) > 0 -> Color.Red
                                        (stockTick.prdyVrss?.toInt() ?: 0) < 0 -> Color.Blue
                                        else  -> Color.Gray
                                    }
                                )
                            )
                        }

                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
        }
    }

    if (errorDialogState.isShown) {
        BaseDialog(
            description = errorDialogState.value.description,
            code = errorDialogState.value.code,
            onClickConfirm = {
                errorDialogState.hide()
                onFinish()
            }
        )
    }
}

private fun String.iscdName(): String {
    return when (this) {
        "005930" -> "삼성 전자"
        "000660" -> "SK 하이닉스"
        else -> "UNKNOWN"
    }
}