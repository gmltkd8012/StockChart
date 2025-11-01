package com.leecoder.stockchart

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leecoder.stockchart.app.screen.MainScreen
import com.leecoder.stockchart.app.screen.SplashScreen
import com.leecoder.stockchart.model.screen.AppIntro

@Composable
fun App(
    onFinish:() -> Unit,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppIntro.Splash.route
    ) {
        composable(AppIntro.Splash.route) {
            SplashScreen(
                callMainScreen = {
                    navController.navigate(AppIntro.Main.route)
                },
                onErrorFinish = onFinish
            )
        }

        composable(AppIntro.Main.route) {
            MainScreen(onFinish = onFinish)
        }
    }


}