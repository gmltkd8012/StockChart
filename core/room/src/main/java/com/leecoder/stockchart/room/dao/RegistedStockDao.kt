package com.leecoder.stockchart.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.leecoder.stockchart.room.entity.RegistedStockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistedStockDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(stock: RegistedStockEntity)

    @Query("SELECT * FROM registedstock")
    fun getRegistedStock(): Flow<List<RegistedStockEntity>>

    @Query("SELECT * FROM registedstock WHERE code = :code")
    fun getRegistedStockByCode(code: String): RegistedStockEntity

    @Delete
    suspend fun delete(stock: RegistedStockEntity)
}