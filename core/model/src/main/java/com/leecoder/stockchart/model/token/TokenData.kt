package com.leecoder.stockchart.model.token

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class TokenData(
    val accessToken: String,
    val tokenExpired: String,
    val tokenType: String,
    val tokenExpiresIn: Int,
)

@Serializable
data class TokenError(
    @SerialName("error_description") val errorDescription: String = "",
    @SerialName("error_code") val errorCode: String = "",
)
