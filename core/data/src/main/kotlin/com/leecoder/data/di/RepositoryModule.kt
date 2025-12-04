package com.leecoder.data.di

import com.leecoder.data.repository.KisInvestmentOverseasRepository
import com.leecoder.data.repository.KisInvestmentOverseasRepositoryImpl
import com.leecoder.data.repository.KoreaAeximRepository
import com.leecoder.data.repository.KoreaAeximRepositoryImpl
import com.leecoder.data.repository.KsInvestmentRepository
import com.leecoder.data.repository.KsInvestmentRepositoryImpl
import com.leecoder.data.repository.room.RoomDatabaseRepository
import com.leecoder.data.repository.room.RoomDatabaseRepositoryImpl
import com.leecoder.data.token.TokenRepository
import com.leecoder.data.token.TokenRepositoryImpl
import com.leecoder.data.repository.WebSocketRepository
import com.leecoder.data.repository.WebSocketRepositoryImpl
import com.leecoder.data.repository.symbol.SymbolRepository
import com.leecoder.data.repository.symbol.SymbolRepositoryImpl
import com.leecoder.data.source.KisInvestmentOverseasDataSource
import com.leecoder.data.source.KoreaAeximDataSource
import com.leecoder.data.source.KsInvestmentDataSource
import com.leecoder.data.source.WebSocketDataSource
import com.leecoder.network.api.TokenApi
import com.leecoder.network.api.WebSocketApi
import com.leecoder.stockchart.appconfig.config.AppConfig
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.room.dao.BollingerDao
import com.leecoder.stockchart.room.dao.ExChangeRateDao
import com.leecoder.stockchart.room.dao.SubscribedStockDao
import com.leecoder.stockchart.room.dao.SymbolDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideTokenRepository(
        tokenApi: TokenApi,
        dataStoreRepository: DataStoreRepository,
    ): TokenRepository = TokenRepositoryImpl(tokenApi, dataStoreRepository)

    @Provides
    @Singleton
    fun provideWebSocketRepository(
        appConfig: AppConfig,
        webSocketApi: WebSocketApi,
        webSocketDataSource: WebSocketDataSource,
        dataStoreRepository: DataStoreRepository,
    ): WebSocketRepository = WebSocketRepositoryImpl(appConfig, webSocketApi, webSocketDataSource, dataStoreRepository)

    @Provides
    fun provideSymbolRepository(
        symbolDao: SymbolDao,
    ): SymbolRepository = SymbolRepositoryImpl(symbolDao)

    @Provides
    fun provideKsInvestmentRepository(
        ksInvestmentDataSource: KsInvestmentDataSource,
    ): KsInvestmentRepository = KsInvestmentRepositoryImpl(ksInvestmentDataSource)

    @Provides
    fun provideRoomDatabaseRepository(
        bollingerDao: BollingerDao,
        subscribedStockDao: SubscribedStockDao,
        exchangeRateDao: ExChangeRateDao,
    ): RoomDatabaseRepository = RoomDatabaseRepositoryImpl(bollingerDao, subscribedStockDao, exchangeRateDao)

    @Provides
    @Singleton
    fun provideKoreaAeximRepository(
        koreaAeximDataSource: KoreaAeximDataSource,
    ): KoreaAeximRepository = KoreaAeximRepositoryImpl(koreaAeximDataSource)

    @Provides
    @Singleton
    fun provideKisInvestmentOverseasRepository(
        kisInvestmentOverseasDataSource: KisInvestmentOverseasDataSource,
    ): KisInvestmentOverseasRepository = KisInvestmentOverseasRepositoryImpl(kisInvestmentOverseasDataSource)
}