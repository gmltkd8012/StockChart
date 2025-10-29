package com.leecoder.stockchart.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.leecoder.stockchart.room.entity.KrxSymbolEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KrxSymbolDao {

    @Insert
    suspend fun insertAll(iscd: List<KrxSymbolEntity>)

    @Query("SELECT * FROM iscds WHERE name LIKE '%' || :name || '%'")
    fun searchKrxSymbol(name: String): Flow<List<KrxSymbolEntity>>
}