package com.leecoder.network.entity.overseas

import com.leecoder.stockchart.model.stock.ChartPriceData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChartPriceResponse (
    @SerialName("output1") val info: ChartPriceResponse.Info,
    @SerialName("output2") val detail: List<ChartPriceResponse.Detail>,
) {
    @Serializable
    data class Info(
        @SerialName("rsym") val rsym: String,
        @SerialName("zdiv") val zdiv: String,
        @SerialName("stim") val stim: String,
        @SerialName("etim") val etim: String,
        @SerialName("sktm") val sktm: String,
        @SerialName("ektm") val ektm: String,
        @SerialName("next") val next: String,
        @SerialName("more") val more: String,
        @SerialName("nrec") val nrec: String,
    )

    @Serializable
    data class Detail(
        @SerialName("tymd") val tymd: String,
        @SerialName("xymd") val xymd: String,
        @SerialName("xhms") val xhms: String,
        @SerialName("kymd") val kymd: String,
        @SerialName("khms") val khms: String,
        @SerialName("open") val open: String,
        @SerialName("high") val high: String,
        @SerialName("low") val low: String,
        @SerialName("last") val last: String,
        @SerialName("evol") val evol: String,
        @SerialName("eamt") val eamt: String,
    )
}

fun ChartPriceResponse.toDataList() =
    this.detail.map { e ->
        ChartPriceData(
            code = this.info.rsym,
            date = e.kymd + e.khms,
            open = e.open,
            high = e.high,
            low = e.low,
            last = e.last
        )
    }

