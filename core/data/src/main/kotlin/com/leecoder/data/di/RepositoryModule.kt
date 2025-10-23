package com.leecoder.data.di

import com.leecoder.data.token.TokenRepository
import com.leecoder.data.token.TokenRepositoryImpl
import com.leecoder.network.api.TokenApi
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
    ): TokenRepository = TokenRepositoryImpl(tokenApi)
}