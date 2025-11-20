package com.leecoder.data.di

import com.leecoder.data.repository.KisInvestmentOverseasRepository
import com.leecoder.data.repository.KisInvestmentOverseasRepositoryImpl
import com.leecoder.data.repository.KoreaAeximRepository
import com.leecoder.data.repository.KoreaAeximRepositoryImpl
import com.leecoder.data.repository.KrxSymbolRepository
import com.leecoder.data.repository.KrxSymbolRepositoryImpl
import com.leecoder.data.repository.KsInvestmentRepository
import com.leecoder.data.repository.KsInvestmentRepositoryImpl
import com.leecoder.data.repository.RoomDatabaseRepository
import com.leecoder.data.repository.RoomDatabaseRepositoryImpl
import com.leecoder.data.token.TokenRepository
import com.leecoder.data.token.TokenRepositoryImpl
import com.leecoder.data.repository.WebSocketRepository
import com.leecoder.data.repository.WebSocketRepositoryImpl
import com.leecoder.data.source.KisInvestmentOverseasDataSource
import com.leecoder.data.source.KoreaAeximDataSource
import com.leecoder.data.source.KsInvestmentDataSource
import com.leecoder.data.source.WebSocketDataSource
import com.leecoder.network.api.TokenApi
import com.leecoder.network.api.WebSocketApi
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.room.dao.BollingerDao
import com.leecoder.stockchart.room.dao.ExChangeRateDao
import com.leecoder.stockchart.room.dao.KrxSymbolDao
import com.leecoder.stockchart.room.dao.SubscribedStockDao
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
        webSocketApi: WebSocketApi,
        webSocketDataSource: WebSocketDataSource,
        dataStoreRepository: DataStoreRepository,
    ): WebSocketRepository = WebSocketRepositoryImpl(webSocketApi, webSocketDataSource, dataStoreRepository)

    @Provides
    fun provideKrxSymbolRepository(
        krxSymbolDao: KrxSymbolDao,
    ): KrxSymbolRepository = KrxSymbolRepositoryImpl(krxSymbolDao)

    @Provides
    fun provideKsInvestmentRepository(
        ksInvestmentDataSource: KsInvestmentDataSource,
    ): KsInvestmentRepository = KsInvestmentRepositoryImpl(ksInvestmentDataSource)

    @Provides
    fun provideRoomDatabaseRepository(
        nasSymbolDao: NasSymbolDao,
        bollingerDao: BollingerDao,
        subscribedStockDao: SubscribedStockDao,
        exchangeRateDao: ExChangeRateDao,
    ): RoomDatabaseRepository = RoomDatabaseRepositoryImpl(nasSymbolDao, bollingerDao, subscribedStockDao, exchangeRateDao)

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