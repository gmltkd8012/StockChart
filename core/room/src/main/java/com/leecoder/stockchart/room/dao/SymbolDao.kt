package com.leecoder.stockchart.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.leecoder.stockchart.room.entity.SymbolEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SymbolDao {

    @Insert
    suspend fun insertAll(iscd: List<SymbolEntity>)

    @Query("SELECT * FROM symbols WHERE name LIKE '%' || :name || '%'")
    fun searchAllSymbol(name: String): Flow<List<SymbolEntity>>

    @Query("""
        SELECT * FROM symbols
        WHERE region = :region 
        AND (code LIKE '%' || :keyword || '%' OR name LIKE '%' || :keyword || '%')
    """)
    fun searchSymbol(region: String, name: String): Flow<List<SymbolEntity>>
}