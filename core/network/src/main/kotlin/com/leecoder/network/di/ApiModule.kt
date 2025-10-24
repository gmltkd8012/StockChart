package com.leecoder.network.di

import com.leecoder.network.StockChartNetwork
import com.leecoder.network.api.TokenApi
import com.leecoder.network.createApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ApiModule {

    @Provides
    @Singleton
    fun provideToeknService(
        stockChartNetwork: StockChartNetwork,
    ): TokenApi {
        return stockChartNetwork.createApi<TokenApi>(
            baseUrl = "https://openapi.koreainvestment.com:9443/"
        )
    }
}