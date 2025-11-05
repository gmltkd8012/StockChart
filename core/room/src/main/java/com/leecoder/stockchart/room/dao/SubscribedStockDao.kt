package com.leecoder.stockchart.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.leecoder.stockchart.room.entity.SubscribedStockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscribedStockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStock(stock: List<SubscribedStockEntity>)

    @Query("SELECT * FROM subscribestock")
    fun getAllSubscribedStocks(): Flow<List<SubscribedStockEntity>>

    @Query("SELECT * FROM subscribestock WHERE code = :code")
    fun getSubscribedStock(code: String): SubscribedStockEntity

    @Delete
    suspend fun deleteStock(stock: SubscribedStockEntity)
}