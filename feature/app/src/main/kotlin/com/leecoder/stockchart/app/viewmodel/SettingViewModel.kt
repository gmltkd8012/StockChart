package com.leecoder.stockchart.app.viewmodel

import android.util.Log
import com.leecoder.stockchart.datastore.const.DataStoreConst
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

    internal fun currentMarketInfo() {
        launch(Dispatchers.IO) {
            dataStoreRepository.currentNasdaqMarketSession.collect() { session ->
                reduceState {
                    copy(
                        marketSession = session.convertMarketSession()
                    )
                }
            }
        }
    }

    private fun String.convertMarketSession(): Pair<String, String> =
        when(this) {
            DataStoreConst.ValueConst.SESSION_DAY_TRADING -> "데이마켓 (나스닥)" to "10:00 ~ 18:00"
            DataStoreConst.ValueConst.SESSION_PRE_MARKET -> "프리마켓 (나스닥)" to "18:00 ~ 23:00"
            DataStoreConst.ValueConst.SESSION_REGULAR -> "정규장 (나스닥)" to "23:30 ~ 06:00 (익일)"
            DataStoreConst.ValueConst.SESSION_AFTER_MARKET -> "에프터마켓 (나스닥)" to "06:00 ~ 10:00"
            else -> "시장 정보 없음" to ""
        }
}

data class SettingState(
    val bollingerSettingValue: String = "",
    val marketSession: Pair<String, String> = Pair("", ""),
)