package com.leecoder.stockchart.datastore.const

object DataStoreConst {
    object DataStoreName {
        const val DATA_STORE_NAME = "stock_chart_datastore"
    }

    object KeyConst {
        const val KR_INVESTMENT_TOKEN = "kr_investment_token"
        const val KR_INVESTMENT_TOKEN_EXPIRED = "kr_investment_token_expired"

        const val KR_INVESTMENT_WEBSOCKET = "kr_investment_websocket"

        const val BOLLINGER_SETTING = "bollinger_setting"

        const val MARKET_INFO = "market_info"
    }

    object ValueConst {
        const val BOLLINGER_DAILY_SETTING = "daily_bollinger"
        const val BOLLINGER_LIVE_SETTING = "live_bollinger"

        const val MARKET_KOSPI = "kospi"
        const val MARKET_NASDAQ = "nasdaq"
    }
}