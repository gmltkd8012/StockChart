package com.leecoder.stockchart.room.di

import com.leecoder.stockchart.room.AppDatabase
import com.leecoder.stockchart.room.dao.BollingerDao
import com.leecoder.stockchart.room.dao.ExChangeRateDao
import com.leecoder.stockchart.room.dao.SubscribedStockDao
import com.leecoder.stockchart.room.dao.SymbolDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun provideSymbolDao(
        database: AppDatabase
    ): SymbolDao = database.symbolDao()

    @Provides
    fun provideSubscribedStockDao(
        database: AppDatabase
    ): SubscribedStockDao = database.subscribedStockDao()

    @Provides
    fun provideBollingerDao(
        database: AppDatabase
    ): BollingerDao = database.bollingerDao()

    @Provides
    fun provideExChangeRateDao(
        database: AppDatabase
    ): ExChangeRateDao = database.exChangeRateDao()
}
