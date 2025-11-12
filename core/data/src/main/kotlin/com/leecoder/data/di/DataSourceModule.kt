package com.leecoder.data.di

import com.leecoder.data.source.KsInvestmentDataSoruceImpl
import com.leecoder.data.source.KsInvestmentDataSource
import com.leecoder.data.source.WebSocketDataSource
import com.leecoder.data.source.WebSocketDataSourceImpl
import com.leecoder.network.api.KisInvestmentApi
import com.leecoder.network.api.WebSocketApi
import com.leecoder.stockchart.appconfig.config.AppConfig
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideWebSocketDataSource(
        @Named("websocket") client: OkHttpClient,
        appConfig: AppConfig,
        webSocketApi: WebSocketApi,
        dataStoreRepository: DataStoreRepository,
    ): WebSocketDataSource = WebSocketDataSourceImpl(client, appConfig, webSocketApi, dataStoreRepository)

    @Provides
    fun provideKrInvestmentDataSource(
        @Named("stock") client: OkHttpClient,
        kisInvestmentApi: KisInvestmentApi,
        dataStoreRepository: DataStoreRepository,
    ): KsInvestmentDataSource = KsInvestmentDataSoruceImpl(client, kisInvestmentApi, dataStoreRepository)
}