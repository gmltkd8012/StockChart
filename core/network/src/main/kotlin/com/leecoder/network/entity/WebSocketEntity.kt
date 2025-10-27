package com.leecoder.network.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WebSocketRequest(
    @SerialName("grant_type") val grantType: String,
    @SerialName("appkey") val appkey: String,
    @SerialName("secretkey") val secretkey: String,
)

@Serializable
data class WebSocketEntity(
    @SerialName("approval_key") val approvalKey: String,
)