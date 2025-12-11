package com.leecoder.network.entity

import com.leecoder.stockchart.model.stock.CurrentPriceData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.sign

@Serializable
data class CurrentPriceNasdaqResponse(
    @SerialName("rt_cd") val rtcd: String,
    @SerialName("msg_cd") val msgcd: String,
    @SerialName("msg1") val msg1: String,
    @SerialName("output") val detail: CurrentPriceNasdaqResponse.Detail,
) {
    @Serializable
    data class Detail(
        @SerialName("rsym") val rsym: String,
        @SerialName("zdiv") val zdiv: String,
        @SerialName("base") val base: String,
        @SerialName("pvol") val pvol: String,
        @SerialName("last") val last: String,
        @SerialName("sign") val sign: String,
        @SerialName("diff") val diff: String,
        @SerialName("rate") val rate: String,
        @SerialName("tvol") val tvol: String,
        @SerialName("tamt") val tamt: String,
        @SerialName("ordy") val ordy: String,
    )
}

fun CurrentPriceNasdaqResponse.toData() =
    CurrentPriceData(
        rsym = this.detail.rsym,
        zdiv = this.detail.zdiv,
        base = this.detail.base,
        pvol = this.detail.pvol,
        last = this.detail.last,
        sign = this.detail.sign,
        diff = this.detail.diff,
        rate = this.detail.rate,
        tvol = this.detail.tvol,
        tamt = this.detail.tamt,
        ordy = this.detail.ordy,
    )
