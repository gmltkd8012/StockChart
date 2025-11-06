package com.leecoder.stockchart.model.ui

data class BollingerUiData(
    val code: String,
    val name: String,
    val upper: Int,
    val middle: Int,
    val lower: Int,
    val cntgHour: String, // 체결 시각
)
