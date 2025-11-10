package com.leecoder.stockchart.datastore.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.leecoder.stockchart.datastore.AppDataStore
import com.leecoder.stockchart.datastore.datastore
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.datastore.repository.DataStoreRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Singleton
    @Provides
    fun provideDataStore(
        application: Application
    ): DataStore<Preferences> = application.datastore

    @Provides
    @Singleton
    fun provideDataStoreRepositoryImpl(
        dataStore: DataStore<Preferences>,
    ): DataStoreRepository = DataStoreRepositoryImpl(dataStore)
}