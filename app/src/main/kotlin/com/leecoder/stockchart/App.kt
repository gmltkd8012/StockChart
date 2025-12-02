package com.leecoder.stockchart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.compose.rememberNavigationEventState
import com.leecoder.stockchart.app.screen.MainScreen
import com.leecoder.stockchart.app.screen.SplashScreen
import com.leecoder.stockchart.app.screen.TestScreen
import com.leecoder.stockchart.model.screen.AppIntro
import com.leecoder.stockchart.ui.navigate.Navigator
import com.leecoder.stockchart.ui.navigate.rememberNavigationState
import com.leecoder.stockchart.ui.navigate.toEntries

@Composable
fun App(
    onFinish:() -> Unit,
) {
    val navigationState = rememberNavigationState(
        startRoute = AppIntro.Splash,
        topLevelRoutes = setOf(AppIntro.Splash, AppIntro.Main)
    )

    val navigator = remember { Navigator(navigationState) }

    val entryProvider = entryProvider {
        featureSplashLoading(
            callMainScreen = { navigator.navigate(AppIntro.Main) },
            onFinish = onFinish,
        )
    }

    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = onFinish,
    )
}

private fun EntryProviderScope<NavKey>.featureSplashLoading(
    callMainScreen: () -> Unit,
    onFinish: () -> Unit,
) {
    entry<AppIntro.Splash> {
        SplashScreen(
            callMainScreen = callMainScreen,
            onErrorFinish = onFinish
        )
    }

    entry<AppIntro.Main> {
        MainScreen(onFinish = onFinish)
    }
}