package com.leecoder.network.entity

import com.leecoder.stockchart.model.token.TokenData

data class TokenEntity(
    val access_token: String,
    val access_token_token_expired: String,
    val token_type: String,
    val expires_in: Int,
)

fun TokenEntity.toData() = TokenData(
    access_token = this.access_token,
    access_token_token_expired = this.access_token_token_expired,
    token_type = this.token_type,
    expires_in = this.expires_in,
)

fun TokenData.toEntity() = TokenEntity(
    access_token = this.access_token,
    access_token_token_expired = this.access_token_token_expired,
    token_type = this.token_type,
    expires_in = this.expires_in,
)