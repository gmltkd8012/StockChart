package com.leecoder.stockchart.model.stock

data class TimeItemChartPriceData(
    val stckCntgHour: String, // 체결 시간
    val stckPrpr: String, // 체결가
)

data class TimeItemChartPriceWithCode(
    val code: String,
    val prices: List<TimeItemChartPriceData>
)
