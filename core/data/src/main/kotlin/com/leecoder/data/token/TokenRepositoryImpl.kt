package com.leecoder.data.token

import com.leecoder.network.api.TokenApi
import com.leecoder.network.entity.toData
import com.leecoder.network.util.NetworkResult
import com.leecoder.stockchart.model.token.TokenData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepositoryImpl @Inject constructor(
    private val tokenApi: TokenApi,
): TokenRepository {

    override suspend fun postToken(
        grantType: String,
        appsecret: String,
        appkey: String
    ): NetworkResult<TokenData> {
        val response = tokenApi.postToken(grantType, appsecret, appkey)

        return if (response.isSuccessful) {
            response.body()?.let { NetworkResult.Success(it.toData()) }
                ?: NetworkResult.Failed(response.code(), response.errorBody()?.string())
        } else {
            NetworkResult.Failed(response.code(), response.errorBody()?.string())
        }
    }
}