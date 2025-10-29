package com.leecoder.stockchart.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leecoder.stockchart.model.symbol.KrxSymbolData

@Entity(
    tableName = "iscds"
)
data class KrxSymbolEntity(
    @PrimaryKey
    val code: String,
    val name: String,
)


fun KrxSymbolEntity.toData() =
    KrxSymbolData(
        code = code,
        name = name,
    )