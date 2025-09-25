package com.leecoder.network.di

import com.leecoder.network.StockChartNetwork
import com.leecoder.network.api.LiveChartApi
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
    fun provideStockChartService(
        stockChartNetwork: StockChartNetwork,
    ): LiveChartApi {
        return stockChartNetwork.createApi<LiveChartApi>(
            baseUrl = "https://query1.finance.yahoo.com/"
        )
    }
}