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
        const val NASDAQ_MARKET_SESSION = "nasdaq_market_session"
    }

    object ValueConst {
        const val BOLLINGER_DAILY_SETTING = "daily_bollinger"
        const val BOLLINGER_LIVE_SETTING = "live_bollinger"

        const val MARKET_KOSPI = "kospi"
        const val MARKET_NASDAQ = "nasdaq"

        // 나스닥 주간/야간 트레이드 코드 (WebSocket 연동용)
        const val NASDAQ_DAY_CODE = "RBAQ"   // 주간: 10:00 ~ 18:00
        const val NASDAQ_NIGHT_CODE = "DNAS" // 야간: 그 외 시간

        // 나스닥 시장 세션 (UI 표시용)
        const val SESSION_DAY_TRADING = "day_trading"       // 주간거래: 10:00 ~ 18:00
        const val SESSION_PRE_MARKET = "pre_market"         // 프리마켓: 18:00 ~ 23:30
        const val SESSION_REGULAR = "regular"               // 정규장: 23:30 ~ 06:00 (익일)
        const val SESSION_AFTER_MARKET = "after_market"     // 애프터마켓: 06:00 ~ 10:00
    }
}