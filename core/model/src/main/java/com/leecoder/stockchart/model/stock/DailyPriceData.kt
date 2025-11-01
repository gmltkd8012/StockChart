package com.leecoder.stockchart.model.stock

import kotlinx.serialization.SerialName

data class DailyPriceData(
    val stckBsopDate: String, // 영업 일자
    val stckClpr: String, // 주식 종가
    val acmlVol: String, // 누적 거래량
)
