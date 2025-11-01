package com.leecoder.stockchart.model.room

data class BollingerData(
    val code: String,
    val name: String,
    val upper: Int,
    val middle: Int,
    val lower: Int
)
