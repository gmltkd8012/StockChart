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

        const val EXCHANGE_RATE_SAVE_TIME = "exchange_rate_save_time"

        const val NASDAQ_TRADE_CODE = "nasdaq_trade_code"
    }

    object ValueConst {
        const val BOLLINGER_DAILY_SETTING = "daily_bollinger"
        const val BOLLINGER_LIVE_SETTING = "live_bollinger"

        const val MARKET_KOSPI = "kospi"
        const val MARKET_NASDAQ = "nasdaq"

        // 나스닥 주간/야간 트레이드 코드
        const val NASDAQ_DAY_CODE = "RBAQ"   // 주간: 10:00 ~ 18:00
        const val NASDAQ_NIGHT_CODE = "DNAS" // 야간: 그 외 시간
    }
}