package com.leecoder.stockchart.design_system.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.leecoder.stockchart.model.screen.Screen

@Composable
fun BaseNavigationBar(
    currentTopLevel: Screen?,
    items: List<Screen>,
    hasAlarm: Boolean,
    onClickNav: (Screen) -> Unit,
) {
    NavigationBar(
        containerColor = Color.Gray,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = item == currentTopLevel

                NavButton (
                    icon = Icons.Default.Settings,
                    title = item.label,
                    screen = item,
                    isSelected = isSelected,
                    isShownBadge = hasAlarm
                ) {
                    onClickNav(item)
                }
            }
        }
    }
}

data class BottomNavItem (
    val route: String,
    val label: String,
    val icon: ImageVector,
)