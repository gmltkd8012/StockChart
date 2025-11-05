package com.leecoder.stockchart.room.di

import com.leecoder.stockchart.room.AppDatabase
import com.leecoder.stockchart.room.dao.BollingerDao
import com.leecoder.stockchart.room.dao.KrxSymbolDao
import com.leecoder.stockchart.room.dao.SubscribedStockDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun provideKrxSymbolDao(
        database: AppDatabase
    ): KrxSymbolDao = database.krxSymbolDao()

    @Provides
    fun provideSubscribedStockDao(
        database: AppDatabase
    ): SubscribedStockDao = database.subscribedStockDao()

    @Provides
    fun provideBollingerDao(
        database: AppDatabase
    ): BollingerDao = database.bollingerDao()
}
