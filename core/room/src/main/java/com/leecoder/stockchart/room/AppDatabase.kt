package com.leecoder.stockchart.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leecoder.stockchart.room.dao.BollingerDao
import com.leecoder.stockchart.room.dao.ExChangeRateDao
import com.leecoder.stockchart.room.dao.KrxSymbolDao
import com.leecoder.stockchart.room.dao.NasSymbolDao
import com.leecoder.stockchart.room.dao.SubscribedStockDao
import com.leecoder.stockchart.room.entity.BollingerEntity
import com.leecoder.stockchart.room.entity.ExChangeRateEntity
import com.leecoder.stockchart.room.entity.KrxSymbolEntity
import com.leecoder.stockchart.room.entity.NasSymbolEntity
import com.leecoder.stockchart.room.entity.SubscribedStockEntity


@Database(
    entities = [
        KrxSymbolEntity::class,
        NasSymbolEntity::class,
        SubscribedStockEntity::class,
        BollingerEntity::class,
        ExChangeRateEntity::class,
    ],
    version = 1,
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun krxSymbolDao(): KrxSymbolDao
    abstract fun nasSymbolDao(): NasSymbolDao
    abstract fun subscribedStockDao(): SubscribedStockDao
    abstract fun bollingerDao(): BollingerDao
    abstract fun exChangeRateDao(): ExChangeRateDao
}