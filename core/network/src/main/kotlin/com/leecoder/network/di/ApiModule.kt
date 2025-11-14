package com.leecoder.network.di

import com.leecoder.network.StockChartNetwork
import com.leecoder.network.api.KisInvestmentApi
import com.leecoder.network.api.KoreaAeximApi
import com.leecoder.network.api.TokenApi
import com.leecoder.network.api.WebSocketApi
import com.leecoder.network.createApi
import com.leecoder.stockchart.appconfig.BuildConfig
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

    @Provides
    @Singleton
    fun provideWebSocketService(
        stockChartNetwork: StockChartNetwork,
    ): WebSocketApi {
        return stockChartNetwork.createApi<WebSocketApi>(
            baseUrl = "https://openapi.koreainvestment.com:9443/"
        )
    }

    @Provides
    @Singleton
    fun provideDaliyPriceApi(
        stockChartNetwork: StockChartNetwork,
    ): KisInvestmentApi {
        return stockChartNetwork.createApi<KisInvestmentApi>(
            baseUrl = "https://openapi.koreainvestment.com:9443"
        )
    }

    @Provides
    @Singleton
    fun provideKoreaAeximApi(
        stockChartNetwork: StockChartNetwork,
    ): KoreaAeximApi {
        return stockChartNetwork.createApi<KoreaAeximApi>(
            baseUrl = BuildConfig.KOREA_AEXIM_BASE_URL
        )
    }
}