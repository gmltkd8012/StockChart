package com.leecoder.data.di

import com.leecoder.data.source.WebSocketDataSource
import com.leecoder.data.source.WebSocketDataSourceImpl
import com.leecoder.network.api.WebSocketApi
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
}