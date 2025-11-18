package com.leecoder.network.entity

import com.leecoder.stockchart.model.stock.CurrentPriceNasdaqData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentPriceNasdaqResponse(
    @SerialName("rt_cd") val rtcd: String,
    @SerialName("msg_cd") val msgcd: String,
    @SerialName("msg1") val msg1: String,
    @SerialName("output") val output: CurrentPriceNasdaqResponse.Output,
) {
    @Serializable
    data class Output(
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

fun CurrentPriceNasdaqResponse.Output.toData() =
    CurrentPriceNasdaqData(
        rsym = rsym,
        zdiv = zdiv,
        base = base,
        pvol = pvol,
        last = last,
        sign = sign,
        diff = diff,
        rate = rate,
        tvol = tvol,
        tamt = tamt,
        ordy = ordy,
    )


@Serializable
data class TimeItemChartPriceNasdaqResponse (
    @SerialName("output1") val output1: TimeItemChartPriceNasdaqResponse.Output1,
    @SerialName("output2") val output2: TimeItemChartPriceNasdaqResponse.Output2,
) {

    @Serializable
    data class Output1(
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
    data class Output2(
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