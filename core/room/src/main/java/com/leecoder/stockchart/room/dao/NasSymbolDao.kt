package com.leecoder.stockchart.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.leecoder.stockchart.room.entity.NasSymbolEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NasSymbolDao {

    @Insert
    suspend fun insertAll(iscd: List<NasSymbolEntity>)

    @Query("SELECT * FROM nas_symbol WHERE code LIKE '%' || :keyword || '%'")
    fun searchNasSymbol(keyword: String): Flow<List<NasSymbolEntity>>
}