package com.leecoder.stockchart.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leecoder.stockchart.model.symbol.NasSymbolData

@Entity(
    tableName = "nas_symbol"
)
data class NasSymbolEntity(
    @PrimaryKey
    val code: String,
    val name: String,
)

fun NasSymbolEntity.toData() =
    NasSymbolData(
        code = code,
        name = name,
    )