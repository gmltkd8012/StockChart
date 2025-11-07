package com.leecoder.stockchart.datastore.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import com.leecoder.stockchart.datastore.AppDataStore
import com.leecoder.stockchart.datastore.const.DataStoreConst
import com.leecoder.stockchart.datastore.extension.toMilliseconds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): DataStoreRepository {

    override val currentKrInvestmentToken: Flow<String?>
        get() = dataStore.data.map { preference -> preference[AppDataStore.Keys.KR_INVESTMENT_TOKEN] }

    override suspend fun refreshKrInvestmentToken(token: String) {
        dataStore.edit { preference ->
            preference[AppDataStore.Keys.KR_INVESTMENT_TOKEN] = token
        }
    }

    override val currentKrInvestmentTokenExpired: Flow<Long?>
        get() = dataStore.data.map { preference -> preference[AppDataStore.Keys.KR_INVESTMENT_TOKEN_EXPIRED] }

    override suspend fun refreshKrInvestmentTokenExpired(tokenExpired: String) {
        dataStore.edit { preference ->
            preference[AppDataStore.Keys.KR_INVESTMENT_TOKEN_EXPIRED] = tokenExpired.toMilliseconds()
        }
    }

    override val currentKrInvestmentWebSocket: Flow<String?>
        get() = dataStore.data.map { preference -> preference[AppDataStore.Keys.KR_INVESTMENT_WEBSOCKET] }

    override suspend fun refreshKrInvestmentWebSocket(webSocket: String) {
        dataStore.edit { preference ->
            preference[AppDataStore.Keys.KR_INVESTMENT_WEBSOCKET] = webSocket
        }
    }

    override val currentBollingerSetting: Flow<String>
        get() = dataStore.data.map { preference ->
            preference[AppDataStore.Keys.BOLLINGER_ALARM_SETTING] ?: DataStoreConst.ValueConst.BOLLINGER_DAILY_SETTING
        }

    override suspend fun updateBollingerSetting(value: String) {
        dataStore.edit { preference ->
            preference[AppDataStore.Keys.BOLLINGER_ALARM_SETTING] = value
        }
    }

    override val currentMarketInfo: Flow<String>
        get() = dataStore.data.map { preference ->
            preference[AppDataStore.Keys.MARKET_INFO] ?: ""
        }

    override suspend fun updateMarketInfo(marketId: String) {
        dataStore.edit { preference ->
            preference[AppDataStore.Keys.MARKET_INFO] = marketId
        }
    }
}