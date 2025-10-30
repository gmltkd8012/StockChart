package com.leecoder.stockchart.app.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.leecoder.stockchart.design_system.component.BaseRegistedBox
import com.leecoder.stockchart.design_system.component.BaseStockBox
import com.leecoder.stockchart.design_system.component.BaseSymbolItem
import com.leecoder.stockchart.design_system.component.BaseTextField
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
    val textFieldState by viewModel.textFieldState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.connectWebSocket()
    }

    LaunchedEffect(state.isConnected) {
        if (state.isConnected) viewModel.initSubcribeStock()
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

    Scaffold(
        modifier = Modifier
            .systemBarsPadding()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        topBar = {
            Spacer(Modifier.height(12.dp))

            BaseTextField(
                textFieldValue = textFieldState,
                hint = "검색어를 입력해주세요.",
                onTextChanged = {
                    viewModel.onQueryChanged(it)
                }
            )
        },
        bottomBar = {}
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding),
        ) {
            if (textFieldState.isNotEmpty()) {
                val searchResult = state.searchResultList ?: emptyList()

                if (searchResult.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "검색할 종목 입력",
                            style = TextStyle(
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                } else {
                    Column {
                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "검색된 종목 결과",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(start = 8.dp)
                        )

                        Spacer(Modifier.height(20.dp))

                        LazyColumn {
                            items(searchResult) { result ->
                                BaseSymbolItem(
                                    name = result.name,
                                    code = result.code,
                                    onClick = { (code, name) ->
                                        viewModel.addSubscribeStock(code, name)
                                        viewModel.onQueryChanged("")
                                    }
                                )

                                Spacer(Modifier.height(12.dp))
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    Column {
                        Spacer(Modifier.height(8.dp))
                        BaseRegistedBox(count = state.registedStock)

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
                                    BaseStockBox(
                                        name =  stockTick.mkscShrnIscd?.iscdName() ?: "UNKNOWN",
                                        code = stockTick.mkscShrnIscd,
                                        tradePrice = stockTick.stckPrpr?.toInt() ?: -1,
                                        priceDiff = stockTick.prdyVrss?.toInt() ?: -1,
                                        onDelete = { code, name ->
                                            //viewModel.addSubscribeStock(code, name)
                                        }
                                    )

                                    Spacer(Modifier.height(12.dp))
                                }
                            }
                        }
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