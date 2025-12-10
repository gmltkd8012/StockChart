package com.leecoder.data.di

import com.leecoder.data.repository.KisInvestmentOverseasRepository
import com.leecoder.data.repository.KisInvestmentOverseasRepositoryImpl
import com.leecoder.data.repository.WebSocketRepository
import com.leecoder.data.repository.WebSocketRepositoryImpl
import com.leecoder.data.repository.room.RoomDatabaseRepository
import com.leecoder.data.repository.room.RoomDatabaseRepositoryImpl
import com.leecoder.data.repository.stock.StockRepository
import com.leecoder.data.repository.stock.StockRepositoryImpl
import com.leecoder.data.token.TokenRepository
import com.leecoder.data.token.TokenRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Binds
    abstract fun bindsWebSocketRepository(
        impl: WebSocketRepositoryImpl
    ): WebSocketRepository

    @Binds
    abstract fun bindsRoomDatabaseRepository(
        impl: RoomDatabaseRepositoryImpl
    ): RoomDatabaseRepository

    @Binds
    @Singleton
    abstract fun bindsStockRepository(
        impl: StockRepositoryImpl
    ): StockRepository

    @Binds
    @Singleton
    abstract fun bindsKisInvestmentOverseasRepository(
        impl: KisInvestmentOverseasRepositoryImpl
    ): KisInvestmentOverseasRepository
}