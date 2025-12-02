package com.leecoder.stockchart.app.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.leecoder.stockchart.app.navigate.featureAlarmScreen
import com.leecoder.stockchart.app.navigate.featureSearchScreen
import com.leecoder.stockchart.app.navigate.featureSettingScreen
import com.leecoder.stockchart.app.navigate.featureSubscribeScreen
import com.leecoder.stockchart.app.viewmodel.MainSideEffect
import com.leecoder.stockchart.app.viewmodel.MainViewModel
import com.leecoder.stockchart.design_system.component.BaseDialog
import com.leecoder.stockchart.design_system.component.BaseNavigationBar
import com.leecoder.stockchart.design_system.component.BaseRegistedBox
import com.leecoder.stockchart.design_system.component.BaseSnackBar
import com.leecoder.stockchart.design_system.component.BaseStockBox
import com.leecoder.stockchart.design_system.component.BaseSymbolItem
import com.leecoder.stockchart.design_system.component.BaseTextField
import com.leecoder.stockchart.model.screen.Screen
import com.leecoder.stockchart.model.screen.Screen.Alarm
import com.leecoder.stockchart.model.screen.Screen.Setting
import com.leecoder.stockchart.model.screen.Screen.Subscribe
import com.leecoder.stockchart.ui.extension.hide
import com.leecoder.stockchart.ui.extension.isShown
import com.leecoder.stockchart.ui.extension.rememberErrorDialogState
import com.leecoder.stockchart.ui.extension.rememberKeyBoardState
import com.leecoder.stockchart.ui.extension.show
import com.leecoder.stockchart.ui.navigate.Navigator
import com.leecoder.stockchart.ui.navigate.rememberNavigationState
import com.leecoder.stockchart.ui.navigate.toEntries

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onFinish: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val textFieldState by viewModel.textFieldState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val navScreen = listOf(Subscribe, Alarm, Setting)

    val navigationState = rememberNavigationState(
        startRoute = Screen.Subscribe,
        topLevelRoutes = setOf(
            Screen.Subscribe,
            Screen.Alarm,
            Screen.Setting,
            Screen.Search,
        )
    )

    val navigator = remember { Navigator(navigationState) }

    val entryProvider = entryProvider {
        featureSubscribeScreen(
            state = derivedStateOf { state },
            onDeletedSymbol = { code, name ->
                viewModel.unSubsctibeStock(code, name)
            }
        )

        featureAlarmScreen(
            state = derivedStateOf { state },
            onDeletedAlarm = { _, _ -> },
        )

        featureSettingScreen()

        featureSearchScreen(
            state = derivedStateOf { state },
            textFieldState = textFieldState,
            onRegistedSymbol = { code, name ->
                viewModel.subscribeStock(code, name)
                viewModel.onQueryChanged("")
            }
        )
    }

    val isShowAlarmBadge = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.collectStockTick()
        viewModel.getCurrentExchangeRate()
        viewModel.initBollingerData()
    }

    LaunchedEffect(state.bollingerLowers) {
        if (state.bollingerLowers.isNotEmpty()) {
            isShowAlarmBadge.value = true
        }
    }

    BackHandler {
        onFinish()
    }

    LaunchedEffect(textFieldState) {
        if (textFieldState.isNotEmpty() && navigationState.topLevelRoute != Screen.Search) {
            navigator.navigate(Screen.Search)
        } else if (textFieldState.isEmpty() && navigationState.topLevelRoute == Screen.Search) {
            navigator.goBack()
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        Log.e("[Leecoder]", "sideEffect -> $sideEffect")
        when (sideEffect) {
            is MainSideEffect.NetworkErrorToast -> {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(sideEffect.description)
            }
            is MainSideEffect.NetworkSuccessToast -> {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(sideEffect.description)
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier
            .systemBarsPadding()
            .background(Color.White),
        topBar = {
            Spacer(Modifier.height(12.dp))

            BaseTextField(
                textFieldValue = textFieldState,
                hint = "종목코드 및 종목명을 입력해주세요.",
                onTextChanged = {
                    viewModel.onQueryChanged(it)
                }
            )
        },
        bottomBar = {
            BaseNavigationBar(
                currentTopLevel = navigationState.topLevelRoute as? Screen,
                items = navScreen,
                hasAlarm = isShowAlarmBadge.value,
                onClickNav = { key ->
                    isShowAlarmBadge.value = false
                    navigator.navigate(key)
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                BaseSnackBar(
                    modifier = Modifier.padding(bottom = 40.dp),
                    message = snackbarData.visuals.message
                )
            }
        },
    ) { innerPadding ->

        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            onBack = onFinish,
            modifier = Modifier
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
        )
    }
}