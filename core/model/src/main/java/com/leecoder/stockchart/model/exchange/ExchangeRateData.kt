package com.leecoder.stockchart.model.exchange

// Room 용
data class ExchangeRateData(
    val curUnit: String, // 통화
    val conName: String, // 국가
    val curName: String, // 통화명
    val exchageRate: String, // 현재 환율
    val prdyVrss: String?, // 전일 대비
    val prdyCtrt: String?, // 전일 대비율
)
