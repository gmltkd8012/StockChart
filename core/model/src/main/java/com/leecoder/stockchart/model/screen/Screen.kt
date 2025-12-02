package com.leecoder.stockchart.model.screen

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(
    val route: String,
    val label: String,
) : NavKey {

    @Serializable
    data object Subscribe: Screen(
        route = "subscribe",
        label = "구독",
    )

    @Serializable
    data object Alarm: Screen(
        route = "alarm",
        label = "알림",
    )

    @Serializable
    data object Search: Screen(
        route = "search",
        label = "검색",
    )

    @Serializable
    data object Setting: Screen(
        route = "setting",
        label = "설정",
    )

    companion object {
        val navScreen = listOf(
            Subscribe,
            Alarm,
            Setting,
        )
    }

    val icon: ImageVector
        get() = when (this) {
            Subscribe -> Icons.Default.Share
            Alarm -> Icons.Default.Notifications
            Setting -> Icons.Default.Settings
            Search -> Icons.Default.Search
        }
}


