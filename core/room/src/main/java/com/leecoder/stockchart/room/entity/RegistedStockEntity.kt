package com.leecoder.stockchart.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leecoder.stockchart.model.stock.RegistedStockData

@Entity(
    tableName = "registedstock"
)
data class RegistedStockEntity(
    @PrimaryKey
    val code: String,
    val name: String,
)

fun RegistedStockEntity.toData() =
    RegistedStockData(
        code = code,
        name = name,
    )

fun RegistedStockData.toEntity() =
    RegistedStockEntity(
        code = code,
        name = name,
    )
