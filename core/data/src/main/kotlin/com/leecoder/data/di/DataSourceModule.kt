package com.leecoder.data.di

import com.leecoder.data.source.KsInvestmentDataSoruceImpl
import com.leecoder.data.source.KsInvestmentDataSource
import com.leecoder.data.source.WebSocketDataSource
import com.leecoder.data.source.WebSocketDataSourceImpl
import com.leecoder.network.api.KisInvestmentApi
import com.leecoder.network.api.WebSocketApi
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    fun provideWebSocketDataSource(
        @Named("websocket") client: OkHttpClient,
        webSocketApi: WebSocketApi,
    ): WebSocketDataSource = WebSocketDataSourceImpl(client, webSocketApi)

    @Provides
    fun provideKrInvestmentDataSource(
        @Named("stock") client: OkHttpClient,
        kisInvestmentApi: KisInvestmentApi,
        dataStoreRepository: DataStoreRepository,
    ): KsInvestmentDataSource = KsInvestmentDataSoruceImpl(client, kisInvestmentApi, dataStoreRepository)
}