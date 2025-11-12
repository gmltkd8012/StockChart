package com.leecoder.stockchart.appconfig.config

interface AppConfig {

    val appKey: String // 앱 키

    val appSecret: String // 앱 시크릿 키

    val baseUrl: String // REST API Base url

    val webSocketUrl: String // WebSocket Base url

    val kospiUrl: String // WebSocket 국장 코드

    val nasdaqUrl: String // WebSocket 미장 코드

    val kospiCode: String // 국장 종목 코드

    val nasdaqCode: String // 미장 종목 코드

    val authToken: String // 토큰 요청 API

    val approvalKey: String // 웹소켓 승인키 API

    val inquire_ccnl: String // 코스피 현재체결가 API

    val inquire_daily_price: String // 코스피 주식현재가 일자별 API

    val inquire_time_itemchartprice: String // 당일분봉조회 API

}