package com.leecoder.stockchart.model.ui

data class StockUiData(
    val code: String? = null,
    val name: String? = null,
    val tradePrice: Double? = null, // 현재 체결가(원)
    val priceDiff: Double? = null, // 전일 대비 상승(원)
)

data class NasdaqUiData(
    val code: String? = null,
    val name: String? = null,
    val kymd: String? = null,   // 한국일자
    val khms: String? = null,   // 한국시간
    val last: Double? = null,   // 현재가
    val diff: Double? = null,   // 전일대비
    val rate: Double? = null,   // 등락율
    val tvol: String? = null,   // 거래량
    val tamt: String? = null,   // 거래대금
)