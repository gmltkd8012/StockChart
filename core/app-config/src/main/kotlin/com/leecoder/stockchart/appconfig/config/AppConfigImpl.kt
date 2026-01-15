package com.leecoder.stockchart.appconfig.config

import android.os.Build
import com.leecoder.stockchart.appconfig.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppConfigImpl @Inject constructor() : AppConfig {

    override val baseUrl: String
        get() = BuildConfig.KIS_BaseUrl

    override val webSocketUrl: String
        get() = BuildConfig.KIS_WebSocketUrl

    override val kospiUrl: String
        get() = BuildConfig.KIS_KOSPI_SOCKET

    override val nasdaqUrl: String
        get() = BuildConfig.KIS_NASDAQ_SOCKET

    override val kospiCode: String
        get() = BuildConfig.KIS_KOSPI_CODE

    override val nasdaqCode: String
        get() = BuildConfig.KIS_NASDAQ_CODE

    override val authToken: String
        get() = BuildConfig.KIS_AuthToken

    override val approvalKey: String
        get() = BuildConfig.KIS_WebSocket_ApprovalKey

    override val inquire_ccnl: String
        get() = BuildConfig.KIS_KOSPI_INQUIRE_CCNL

    override val inquire_daily_price: String
        get() = BuildConfig.KIS_KOSPI_INQUIRE_DAILY_PRICE

    override val inquire_time_itemchartprice: String
        get() = BuildConfig.KIS_KOSPI_INQUIRE_TIME_ITEMCHARTPRICE

    override val korea_aexim_api_key: String
        get() = BuildConfig.KOREA_AEXIM_API_KEY

    override val inquire_ccnl_nas: String
        get() = BuildConfig.KIS_NASDAQ_INQUIRE_CCNL

    override val inquire_time_itemchartprice_nas: String
        get() = BuildConfig.KIS_NASDAQ_INQUIRE_TIME_ITEMCHARTPRICE
}