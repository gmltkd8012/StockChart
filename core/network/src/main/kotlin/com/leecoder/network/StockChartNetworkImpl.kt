package com.leecoder.network

import retrofit2.Retrofit
import javax.inject.Named

class StockChartNetworkImpl(
    @Named("stock") private val retrofit: Retrofit.Builder,
): StockChartNetwork {

    override fun <T> create(baseUrl: String, service: Class<T>): T {
        return retrofit
            .baseUrl(baseUrl)
            .build()
            .create(service)
    }
}