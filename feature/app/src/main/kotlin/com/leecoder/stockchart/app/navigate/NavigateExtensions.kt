package com.leecoder.stockchart.app.navigate

import androidx.compose.runtime.State
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.leecoder.stockchart.app.screen.AlarmScreen
import com.leecoder.stockchart.app.screen.SearchScreen
import com.leecoder.stockchart.app.screen.SettingScreen
import com.leecoder.stockchart.app.screen.SubscribeScreen
import com.leecoder.stockchart.app.viewmodel.MainState
import com.leecoder.stockchart.model.screen.Screen
import java.util.Map.entry

internal fun EntryProviderScope<NavKey>.featureSubscribeScreen(
    state: State<MainState>,
    onDeletedSymbol: (String, String) -> Unit,
) {
    entry<Screen.Subscribe> {
        SubscribeScreen(
            stockTick = state.value.stockTickMap?.values?.toList() ?: emptyList(),
            exchangeRates = state.value.exchangeRates,
            onDeletedSymbol = onDeletedSymbol
        )
    }
}

internal fun EntryProviderScope<NavKey>.featureAlarmScreen(
    state: State<MainState>,
    onDeletedAlarm: (String, String) -> Unit,
) {
    entry<Screen.Alarm> {
        AlarmScreen(
            bollingers = state.value.bollingerLowers.values.toList(),
            maxCount = state.value.stockTickMap?.size ?: 0,
            onDeletedAlarm = onDeletedAlarm
        )
    }
}

internal fun EntryProviderScope<NavKey>.featureSettingScreen() {
    entry<Screen.Setting> {
        SettingScreen()
    }
}

internal fun EntryProviderScope<NavKey>.featureSearchScreen(
    state: State<MainState>,
    textFieldState: String,
    onRegistedSymbol: (String, String) -> Unit,
) {
    entry<Screen.Search> {
        SearchScreen(
            keyword = textFieldState,
            searchResult = state.value.searchResultList ?: emptyList(),
            onRegistedSymbol = onRegistedSymbol,
        )
    }
}