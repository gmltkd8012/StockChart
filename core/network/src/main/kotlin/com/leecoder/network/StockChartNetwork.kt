package com.leecoder.network

interface StockChartNetwork {

    fun <T> create(baseUrl: String, service: Class<T>): T
}

inline fun <reified T> StockChartNetwork.createApi(baseUrl: String): T {
    return create(baseUrl, T::class.java)
}
