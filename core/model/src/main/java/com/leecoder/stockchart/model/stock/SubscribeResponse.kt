package com.leecoder.stockchart.model.stock

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscribeResponse(
    @SerialName("header") val header: Header,
    @SerialName("body") val body: Body
) {
    @Serializable
    data class Header(
        @SerialName("tr_id") val trid: String,
        @SerialName("tr_key") val trkey: String,
        @SerialName("encrypt") val encrypt: String
    )

    @Serializable
    data class Body(
        @SerialName("rt_cd") val rtcd: String,
        @SerialName("msg_cd") val msgcd: String,
        @SerialName("msg1") val msg1: String,
        @SerialName("output") val output: Output
    ) {

        @Serializable
        data class Output(
            @SerialName("iv") val iv: String,
            @SerialName("key") val key: String
        )
    }
}
