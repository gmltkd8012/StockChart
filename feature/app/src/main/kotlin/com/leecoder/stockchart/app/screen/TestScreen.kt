package com.leecoder.stockchart.app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable

@Serializable data object Home1 : NavKey

@Serializable
data class Setting(val id: String) : NavKey

@Composable
fun TestScreen(

) {
    val backStack = rememberNavBackStack(Home1)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Home1> {
                TestContentBox("Home", onClick = { backStack.add(Setting("123")) })
            }
            entry<Setting>(
                metadata = mapOf("extraDataKey" to "extraDataValue")
            ) { key ->
                TestContentBox("Setting -> ${key.id}", onClick = { backStack.removeLastOrNull() })
            }
        }
    )
}

@Composable
fun TestContentBox(
    screenName: String,
    onClick:() -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize().background(color = Color.White).clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = screenName,
            color = Color.Black,
        )
    }
}

