package com.leecoder.stockchart.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.leecoder.stockchart.room.entity.BollingerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BollingerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bollinger: List<BollingerEntity>)

    @Insert
    suspend fun insert(bollinger: BollingerEntity)

    @Query("SELECT * FROM bollinger")
    fun getBollingers(): Flow<List<BollingerEntity>>

    @Delete
    suspend fun delete(bollinger: BollingerEntity)
}