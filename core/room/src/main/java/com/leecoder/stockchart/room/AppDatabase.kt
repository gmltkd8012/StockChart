package com.leecoder.stockchart.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leecoder.stockchart.room.dao.KrxSymbolDao
import com.leecoder.stockchart.room.dao.RegistedStockDao
import com.leecoder.stockchart.room.entity.KrxSymbolEntity
import com.leecoder.stockchart.room.entity.RegistedStockEntity


@Database(
    entities = [
        KrxSymbolEntity::class,
        RegistedStockEntity::class,
    ],
    version = 1,
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun krxSymbolDao(): KrxSymbolDao
    abstract fun registedStockDao(): RegistedStockDao
}