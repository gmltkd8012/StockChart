package com.leecoder.stockchart.app.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leecoder.stockchart.app.viewmodel.MainSideEffect
import com.leecoder.stockchart.app.viewmodel.MainViewModel
import com.leecoder.stockchart.design_system.component.BaseDialog
import com.leecoder.stockchart.design_system.component.BaseNavigationBar
import com.leecoder.stockchart.design_system.component.BaseRegistedBox
import com.leecoder.stockchart.design_system.component.BaseStockBox
import com.leecoder.stockchart.design_system.component.BaseSymbolItem
import com.leecoder.stockchart.design_system.component.BaseTextField
import com.leecoder.stockchart.model.screen.Screen
import com.leecoder.stockchart.ui.extension.hide
import com.leecoder.stockchart.ui.extension.isShown
import com.leecoder.stockchart.ui.extension.rememberErrorDialogState
import com.leecoder.stockchart.ui.extension.rememberKeyBoardState
import com.leecoder.stockchart.ui.extension.show

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onFinish: () -> Unit,
) {
    val navController = rememberNavController()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val textFieldState by viewModel.textFieldState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.connectWebSocket()
    }

    LaunchedEffect(state.isConnected) {
        if (state.isConnected) viewModel.initSubcribeStock()
    }

    BackHandler {
        onFinish()
    }

    Scaffold(
        modifier = Modifier
            .systemBarsPadding()
            .background(Color.White),
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
        bottomBar = {
            BaseNavigationBar(navController, Screen.allScreen)
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Subscribe.route,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
        ) {
            composable(Screen.Subscribe.route) {
                SubscribeScreen(
                    textFieldState = textFieldState,
                    searchResult = state.searchResultList ?: emptyList(),
                    stockTick = state.stockTickMap?.values?.toList() ?: emptyList(),
                    onRegistedSymbol = { code, name ->
                        viewModel.subscribeStock(code, name)
                        viewModel.onQueryChanged("")
                    },
                    onDeletedSymbol = { code, name ->
                        viewModel.unSubsctibeStock(code, name)
                    }

                )
            }
            composable(Screen.Alarm.route) {
                AlarmScreen()
            }
        }
    }
}