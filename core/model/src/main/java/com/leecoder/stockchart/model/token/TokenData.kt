package com.leecoder.stockchart.model.token

data class TokenData(
    val accessToken: String,
    val tokenExpired: String,
    val tokenType: String,
    val tokenExpiresIn: Int,
)
