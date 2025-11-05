package com.leecoder.stockchart.model.stock

data class SubscribedStockData(
    val code: String,
    val name: String,
    val price: Int = -1,
)
