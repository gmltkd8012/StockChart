package com.leecoder.stockchart.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.leecoder.stockchart.model.exchange.ExchangeRateData

@Entity(
    tableName = "exchange_rate"
)
data class ExChangeRateEntity(
    @PrimaryKey
    val curUnit: String, // 통화
    val conName: String, // 국가
    val curName: String, // 통화명
    val prdyVrss: String?, // 전일 대비
    val prdyCtrt: String?, // 전일 대비율
)

fun List<ExChangeRateEntity>.toDataList() =
    this.map {
        ExchangeRateData(
            curUnit = it.curUnit,
            conName = it.conName,
            curName = it.curName,
            prdyVrss = it.prdyVrss ?: "",
            prdyCtrt = it.prdyCtrt ?: ""
        )
    }

fun List<ExchangeRateData>.toEntityList() =
    this.map {
        ExChangeRateEntity(
            curUnit = it.curUnit,
            conName = it.conName,
            curName = it.curName,
            prdyVrss = it.prdyVrss,
            prdyCtrt = it.prdyCtrt,
        )
    }

