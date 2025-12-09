package com.leecoder.data.di

import com.leecoder.data.repository.WebSocketRepository
import com.leecoder.data.repository.WebSocketRepositoryImpl
import com.leecoder.data.repository.room.RoomDatabaseRepository
import com.leecoder.data.repository.room.RoomDatabaseRepositoryImpl
import com.leecoder.data.token.TokenRepository
import com.leecoder.data.token.TokenRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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
}