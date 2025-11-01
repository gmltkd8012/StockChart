package com.leecoder.data.di

import com.leecoder.data.repository.KrxSymbolRepository
import com.leecoder.data.repository.KrxSymbolRepositoryImpl
import com.leecoder.data.repository.KsInvestmentRepository
import com.leecoder.data.repository.KsInvestmentRepositoryImpl
import com.leecoder.data.repository.RegistedStockRepository
import com.leecoder.data.repository.RegistedStockRepositoryImpl
import com.leecoder.data.token.TokenRepository
import com.leecoder.data.token.TokenRepositoryImpl
import com.leecoder.data.repository.WebSocketRepository
import com.leecoder.data.repository.WebSocketRepositoryImpl
import com.leecoder.data.source.KsInvestmentDataSource
import com.leecoder.data.source.WebSocketDataSource
import com.leecoder.network.api.TokenApi
import com.leecoder.network.api.WebSocketApi
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.room.dao.KrxSymbolDao
import com.leecoder.stockchart.room.dao.RegistedStockDao
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

    @Provides
    fun provideKrxSymbolRepository(
        krxSymbolDao: KrxSymbolDao,
    ): KrxSymbolRepository = KrxSymbolRepositoryImpl(krxSymbolDao)

    @Provides
    fun provideRegistedStockRepository(
        registedStockDao: RegistedStockDao,
    ): RegistedStockRepository = RegistedStockRepositoryImpl(registedStockDao)

    @Provides
    fun provideKsInvestmentRepository(
        ksInvestmentDataSource: KsInvestmentDataSource,
    ): KsInvestmentRepository = KsInvestmentRepositoryImpl(ksInvestmentDataSource)
}