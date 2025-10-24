package com.leecoder.data.di

import com.leecoder.data.token.TokenRepository
import com.leecoder.data.token.TokenRepositoryImpl
import com.leecoder.network.api.TokenApi
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
        dataStoreRepository: DataStoreRepository
    ): TokenRepository = TokenRepositoryImpl(tokenApi, dataStoreRepository)
}