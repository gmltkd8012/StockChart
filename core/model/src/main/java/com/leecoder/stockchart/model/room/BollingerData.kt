package com.leecoder.stockchart.model.room

data class BollingerData(
    val code: String,
    val name: String,
    val upper: Double,
    val middle: Double,
    val lower: Double
)
