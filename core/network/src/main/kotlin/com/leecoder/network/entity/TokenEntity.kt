package com.leecoder.network.entity

import com.leecoder.stockchart.model.token.TokenData
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(
    @SerialName("grant_type") val grantType: String,
    @SerialName("appsecret") val appsecret: String,
    @SerialName("appkey") val appkey: String,
)

@Serializable
data class TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("access_token_token_expired") val tokenExpired: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val tokenExpiresIn: Int,
)

fun TokenResponse.toData() = TokenData(
    accessToken = this.accessToken,
    tokenExpired = this.tokenExpired,
    tokenType = this.tokenType,
    tokenExpiresIn = this.tokenExpiresIn,
)

fun TokenData.toEntity() = TokenResponse(
    accessToken = this.accessToken,
    tokenExpired = this.tokenExpired,
    tokenType = this.tokenType,
    tokenExpiresIn = this.tokenExpiresIn,
)