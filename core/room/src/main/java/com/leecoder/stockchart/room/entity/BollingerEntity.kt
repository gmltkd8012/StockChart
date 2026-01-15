package com.leecoder.stockchart.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leecoder.stockchart.model.room.BollingerData

@Entity(
    tableName = "bollinger"
)
data class BollingerEntity(
    @PrimaryKey
    val code: String,
    val name: String,
    val upper: Double,
    val middle: Double,
    val lower: Double,
)

fun BollingerEntity.toData() = BollingerData(
    code = code,
    name = name,
    upper = upper,
    middle = middle,
    lower = lower
)

fun BollingerData.toEntity() = BollingerEntity(
    code = code,
    name = name,
    upper = upper,
    middle = middle,
    lower = lower
)
