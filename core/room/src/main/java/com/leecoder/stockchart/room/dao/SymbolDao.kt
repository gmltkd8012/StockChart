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
        AND (REPLACE(code, ' ', '') LIKE '%' || REPLACE(:keyword, ' ', '') || '%' OR REPLACE(name, ' ', '') LIKE '%' || REPLACE(:keyword, ' ', '') || '%')
        ORDER BY 
            CASE WHEN INSTR(UPPER(code), UPPER(:keyword)) > 0 THEN INSTR(UPPER(code), UPPER(:keyword))
                 WHEN INSTR(UPPER(name), UPPER(:keyword)) > 0 THEN INSTR(UPPER(name), UPPER(:keyword)) + 1000
                 ELSE 9999
            END ASC,
            code ASC,
            name ASC
    """)
    fun searchSymbol(region: String, keyword: String): Flow<List<SymbolEntity>>
}