package com.leecoder.stockchart.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leecoder.stockchart.room.dao.KrxSymbolDao
import com.leecoder.stockchart.room.entity.KrxSymbolEntity


@Database(
    entities = [
        KrxSymbolEntity::class,
    ],
    version = 1,
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun krxSymbolDao(): KrxSymbolDao
}