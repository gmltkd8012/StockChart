package com.leecoder.stockchart.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.leecoder.stockchart.room.entity.ExChangeRateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExChangeRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rates: List<ExChangeRateEntity>)

    @Query("SELECT * FROM exchange_rate")
    fun getAllExChangeRates(): Flow<List<ExChangeRateEntity>>

    @Delete
    suspend fun deleteAll(rates: List<ExChangeRateEntity>)
}