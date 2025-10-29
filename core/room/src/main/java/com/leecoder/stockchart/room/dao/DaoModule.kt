package com.leecoder.stockchart.room.dao

import com.leecoder.stockchart.room.AppDatabase
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
    fun provideRegistedStockDao(
        database: AppDatabase
    ): RegistedStockDao = database.registedStockDao()
}
