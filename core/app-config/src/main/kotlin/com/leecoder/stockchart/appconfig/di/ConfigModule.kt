package com.leecoder.stockchart.appconfig.di

import com.leecoder.stockchart.appconfig.config.AppConfig
import com.leecoder.stockchart.appconfig.config.AppConfigImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {

    @Provides
    @Singleton
    fun provideAppConfig(): AppConfig = AppConfigImpl()
}