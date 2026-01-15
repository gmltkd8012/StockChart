package com.leecoder.stockchart.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.leecoder.stockchart.datastore.const.DataStoreConst
import com.leecoder.stockchart.datastore.const.DataStoreConst.DataStoreName.DATA_STORE_NAME


val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

class AppDataStore {

    object Keys {
        val KR_INVESTMENT_TOKEN = stringPreferencesKey(name = DataStoreConst.KeyConst.KR_INVESTMENT_TOKEN)
        val KR_INVESTMENT_TOKEN_EXPIRED = longPreferencesKey(DataStoreConst.KeyConst.KR_INVESTMENT_TOKEN_EXPIRED)

        val KR_INVESTMENT_WEBSOCKET = stringPreferencesKey(DataStoreConst.KeyConst.KR_INVESTMENT_WEBSOCKET)

        val BOLLINGER_ALARM_SETTING = stringPreferencesKey(DataStoreConst.KeyConst.BOLLINGER_SETTING)

        val MARKET_INFO = stringPreferencesKey(DataStoreConst.KeyConst.MARKET_INFO)

        val EXCHANGE_RATE_SAVE_TIME = longPreferencesKey(DataStoreConst.KeyConst.EXCHANGE_RATE_SAVE_TIME)
        val EXCHANGE_RATE_USD = stringPreferencesKey(DataStoreConst.KeyConst.EXCHANGE_RATE_USD)

        val NASDAQ_TRADE_CODE = stringPreferencesKey(DataStoreConst.KeyConst.NASDAQ_TRADE_CODE)
        val NASDAQ_MARKET_SESSION = stringPreferencesKey(DataStoreConst.KeyConst.NASDAQ_MARKET_SESSION)

        val APP_KEY = stringPreferencesKey(DataStoreConst.KeyConst.APP_KEY)
        val APP_SECRET = stringPreferencesKey(DataStoreConst.KeyConst.APP_SECRET)
    }
}