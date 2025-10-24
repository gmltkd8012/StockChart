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

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Provides
    fun provideDataStore(
        application: Application
    ): DataStore<Preferences> = application.datastore

    @Provides
    fun provideDataStoreRepositoryImpl(
        dataStore: DataStore<Preferences>,
    ): DataStoreRepository = DataStoreRepositoryImpl(dataStore)
}