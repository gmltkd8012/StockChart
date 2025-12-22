package com.leecoder.data.source

import com.leecoder.network.api.TokenApi
import com.leecoder.network.entity.TokenRequest
import com.leecoder.network.entity.toData
import com.leecoder.stockchart.model.token.TokenData
import com.leecoder.stockchart.model.token.TokenError
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSourceImpl @Inject constructor(
    private val tokenApi: TokenApi
) : AuthDataSource {

    override suspend fun postToken(
        grantType: String,
        appsecret: String,
        appkey: String
    ): Result<TokenData> {
        return try {
            val request = TokenRequest(grantType, appsecret, appkey)
            val response = tokenApi.postToken(request)

            if (response.isSuccessful) {
                val body = response.body()
                    ?: return Result.failure(Exception("Response body is null"))
                Result.success(body.toData())
            } else {
                val errorBody = response.errorBody()?.string()
                val tokenError = errorBody?.let {
                    try {
                        Json.decodeFromString<TokenError>(it)
                    } catch (e: Exception) {
                        TokenError(errorDescription = it)
                    }
                } ?: TokenError()
                Result.failure(TokenException(tokenError))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class TokenException(val tokenError: TokenError) : Exception(tokenError.errorDescription)
