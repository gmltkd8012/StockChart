package com.leecoder.stockchart.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leecoder.stockchart.model.stock.SubscribedStockData

@Entity(
    tableName = "subscribestock"
)
data class SubscribedStockEntity(
    @PrimaryKey
    val code: String,
    val name: String,
    val price: String,
)

fun SubscribedStockEntity.toData() =
    SubscribedStockData(
        code = code,
        name = name,
        price = price,
    )

fun SubscribedStockData.toEntity() =
    SubscribedStockEntity(
        code = code,
        name = name,
        price = price,
    )
