import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.library)
    alias(libs.plugins.lib.flavor)
    alias(libs.plugins.all.hilt)
}

android {
    namespace = "com.leecoder.stockchart.appconfig"

    val appKey: String = System.getenv("APP_KEY") ?: gradleLocalProperties(rootDir, providers).getProperty("app_key")
    val appSecret: String = System.getenv("APP_SECRET") ?: gradleLocalProperties(rootDir, providers).getProperty("app_secret")

    val kisBaseUrl: String = System.getenv("KIS_BASE_URL") ?: gradleLocalProperties(rootDir, providers).getProperty("kis_base_url")
    val kisWebsocketBaseUrl: String = System.getenv("KIS_WEBSOCKET_BASE_URL") ?: gradleLocalProperties(rootDir, providers).getProperty("kis_websocket_base_url")

    val kisWebSocketKospiEntryPoint: String = System.getenv("KIS_WEBSOCKET_KOSPI_ENTRY_POINT") ?: gradleLocalProperties(rootDir, providers).getProperty("kis_websocket_kospi_entry_point")
    val kisWebSocketNasdaqEntryPoint: String = System.getenv("KIS_WEBSOCKET_NASDAQ_ENTRY_POINT") ?: gradleLocalProperties(rootDir, providers).getProperty("kis_websocket_nasdaq_entry_point")

    val kisWebSocketKospiCode: String = System.getenv("KIS_WEBSOCKET_KOSPI_CODE") ?: gradleLocalProperties(rootDir, providers).getProperty("kis_websocket_kospi_code")
    val kisWebSocketNasdaqCode: String = System.getenv("KIS_WEBSOCKET_NASDAQ_CODE") ?: gradleLocalProperties(rootDir, providers).getProperty("kis_websocket_nasdaq_code")

    val kisAuthTokenProvideEntryPoint: String = System.getenv("KIS_AUTH_TOKEN_PROVIDE_ENTRY_POINT") ?: gradleLocalProperties(rootDir, providers).getProperty("kis_auth_token_provide_entry_point")
    val kisWebSocketApprovalKeyEntryPoint: String = System.getenv("KIS_WEBSOCKET_APPROVAL_KEY_ENTRY_POINT") ?: gradleLocalProperties(rootDir, providers).getProperty("kis_websocket_approval_key_entry_point")

    val kisKospiInquireCcnlEntryPoint: String = System.getenv("KIS_KOSPI_INQUIRE_CCNL_ENTRY_POINT") ?: gradleLocalProperties(rootDir, providers).getProperty("kis_kospi_inquire_ccnl")
    val kisKospiInquireDailyPriceEntryPoint: String = System.getenv("KIS_KOSPI_INQUIRE_DAILY_PRICE_ENTRY_POINT") ?: gradleLocalProperties(rootDir, providers).getProperty("kis_kospi_inquire_daily_price")
    val kisKospiInquireTimeItemChartPriceEntryPoint: String = System.getenv("KIS_KOSPI_INQUIRE_TIME_ITEMCHARTPRICE_ENTRY_POINT") ?: gradleLocalProperties(rootDir, providers).getProperty("kis_kospi_inquire_time_itemchartprice")


    defaultConfig {
        buildConfigField("String", "AppKey", appKey)
        buildConfigField("String", "AppSecret", appSecret)

        buildConfigField("String", "KIS_BaseUrl", kisBaseUrl)
        buildConfigField("String", "KIS_WebSocketUrl", kisWebsocketBaseUrl)

        buildConfigField("String", "KIS_KOSPI_SOCKET", kisWebSocketKospiEntryPoint)
        buildConfigField("String", "KIS_NASDAQ_SOCKET", kisWebSocketNasdaqEntryPoint)

        buildConfigField("String", "KIS_KOSPI_CODE", kisWebSocketKospiCode)
        buildConfigField("String", "KIS_NASDAQ_CODE", kisWebSocketNasdaqCode)

        buildConfigField("String", "KIS_AuthToken", kisAuthTokenProvideEntryPoint)
        buildConfigField("String", "KIS_WebSocket_ApprovalKey", kisWebSocketApprovalKeyEntryPoint)

        buildConfigField("String", "KIS_KOSPI_INQUIRE_CCNL", kisKospiInquireCcnlEntryPoint)
        buildConfigField("String", "KIS_KOSPI_INQUIRE_DAILY_PRICE", kisKospiInquireDailyPriceEntryPoint)
        buildConfigField("String", "KIS_KOSPI_INQUIRE_TIME_ITEMCHARTPRICE", kisKospiInquireTimeItemChartPriceEntryPoint)
    }
}

dependencies {}