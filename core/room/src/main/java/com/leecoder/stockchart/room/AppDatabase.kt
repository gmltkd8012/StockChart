package com.leecoder.stockchart.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leecoder.stockchart.room.dao.BollingerDao
import com.leecoder.stockchart.room.dao.ExChangeRateDao
import com.leecoder.stockchart.room.dao.SubscribedStockDao
import com.leecoder.stockchart.room.dao.SymbolDao
import com.leecoder.stockchart.room.entity.BollingerEntity
import com.leecoder.stockchart.room.entity.ExChangeRateEntity
import com.leecoder.stockchart.room.entity.SubscribedStockEntity
import com.leecoder.stockchart.room.entity.SymbolEntity


@Database(
    entities = [
        SymbolEntity::class,
        SubscribedStockEntity::class,
        BollingerEntity::class,
        ExChangeRateEntity::class,
    ],
    version = 1,
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun symbolDao(): SymbolDao
    abstract fun subscribedStockDao(): SubscribedStockDao
    abstract fun bollingerDao(): BollingerDao
    abstract fun exChangeRateDao(): ExChangeRateDao
}