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
    val last: Double? = null,   // 현재가
    val diff: Double? = null,   // 전일대비
)