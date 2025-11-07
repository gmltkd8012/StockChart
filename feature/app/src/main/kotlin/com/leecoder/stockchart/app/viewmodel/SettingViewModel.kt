package com.leecoder.stockchart.app.viewmodel

import android.util.Log
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.ui.base.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
): StateViewModel<SettingState, Nothing>(SettingState()) {

    internal fun currentBollingerSetting() {
        launch(Dispatchers.IO) {
            val bollingerSettingValue = dataStoreRepository.currentBollingerSetting.first()

            Log.i("lynn", "[currentBollingerSetting]: $bollingerSettingValue ")
            reduceState {
                copy(bollingerSettingValue = bollingerSettingValue)
            }
        }
    }

    internal fun changedBollingerSetting(value: String) {
        launch(Dispatchers.IO) {
            dataStoreRepository.updateBollingerSetting(value)
        }
    }
}

data class SettingState(
    val bollingerSettingValue: String = "",
    val marketInfo: String = "",
)