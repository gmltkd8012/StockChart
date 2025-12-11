package com.leecoder.stockchart.model.stock

data class ChartPriceData(
    val code: String, // 종목 코드
    val date: String, // 거래 시각 - YYYYMMDDHHMMSS
    val open: String, // 시가 - 계산 사용
    val high: String, // 고가
    val low: String, // 저가
    val last: String, // 종가
)
