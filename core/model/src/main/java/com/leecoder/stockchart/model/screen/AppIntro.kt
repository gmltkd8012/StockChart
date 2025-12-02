package com.leecoder.stockchart.model.screen

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class AppIntro(): NavKey {

    @Serializable
    data object Splash: AppIntro()

    @Serializable
    data object Main: AppIntro()
}