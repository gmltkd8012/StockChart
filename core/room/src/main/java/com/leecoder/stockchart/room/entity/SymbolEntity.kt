package com.leecoder.stockchart.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leecoder.stockchart.model.symbol.SymbolData

@Entity(
    tableName = "symbols"
)
data class SymbolEntity(
    @PrimaryKey
    val code: String,
    val name: String,
    val region: String,
)


fun SymbolEntity.toData() =
    SymbolData(
        code = code,
        name = name,
        region = region,
    )