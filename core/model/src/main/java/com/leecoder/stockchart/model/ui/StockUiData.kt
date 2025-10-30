package com.leecoder.stockchart.model.ui

data class StockUiData(
    val code: String? = null,
    val name: String? = null,
    val tradePrice: Int? = null, // 현재 체결가(원)
    val priceDiff: Int? = null, // 전일 대비 상승(원)
)
