package com.leecoder.stockchart.model.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    data object Subscribe: Screen(
        route = "subscribe",
        label = "구독",
        icon = Icons.Default.Share,
    )

    data object Alarm: Screen(
        route = "alarm",
        label = "알림",
        icon = Icons.Default.Notifications,
    )

    companion object {
        val allScreen = listOf(
            Subscribe,
            Alarm,
        )
    }
}