package com.leecoder.stockchart.util.calculator

interface BollingerAggregator<T> {

    suspend fun initWith(list: List<T>)
}