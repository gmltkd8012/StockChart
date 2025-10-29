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

@Serializable
data class WebSocketApprovalHeader(
    @SerialName("approval_key") val approvalKey: String,
    @SerialName("custtype") val custType: String,
    @SerialName("tr_type") val trType: String,
    @SerialName("content-type") val contentType: String,
)

@Serializable
data class WebSocketApprovalBody(
    @SerialName("input") val input: WebSocketApprovalInput,
)

@Serializable
data class WebSocketApprovalInput(
    @SerialName("tr_id") val id: String,
    @SerialName("tr_key") val key: String,
)

@Serializable
data class WebSocketApproval(
    @SerialName("header") val header: WebSocketApprovalHeader,
    @SerialName("body") val body: WebSocketApprovalBody,
)