package com.leecoder.stockchart.model.screen

import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppIntro(
    val route: String,
) {
    data object Splash: AppIntro(
        route = "splash",
    )

    data object Main: AppIntro(
        route = "main",
    )
}