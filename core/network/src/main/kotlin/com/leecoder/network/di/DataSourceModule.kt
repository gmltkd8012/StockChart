package com.leecoder.network.di

import com.leecoder.network.source.KisInvestmentOverseasDataSource
import com.leecoder.network.source.KisInvestmentOverseasDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindsKisInvestmentOverseasDataSource(
        impl: KisInvestmentOverseasDataSourceImpl
    ): KisInvestmentOverseasDataSource
}