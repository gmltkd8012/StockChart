package com.leecoder.data.di

import com.leecoder.data.token.TokenRepository
import com.leecoder.data.token.TokenRepositoryImpl
import com.leecoder.data.repository.WebSocketRepository
import com.leecoder.data.repository.WebSocketRepositoryImpl
import com.leecoder.data.source.WebSocketDataSource
import com.leecoder.network.api.TokenApi
import com.leecoder.network.api.WebSocketApi
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideTokenRepository(
        tokenApi: TokenApi,
        dataStoreRepository: DataStoreRepository,
    ): TokenRepository = TokenRepositoryImpl(tokenApi, dataStoreRepository)

    @Provides
    fun provideWebSocketRepository(
        webSocketApi: WebSocketApi,
        webSocketDataSource: WebSocketDataSource,
        dataStoreRepository: DataStoreRepository,
    ): WebSocketRepository = WebSocketRepositoryImpl(webSocketApi, webSocketDataSource, dataStoreRepository)
}