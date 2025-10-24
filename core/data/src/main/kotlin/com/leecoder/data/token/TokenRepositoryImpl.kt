package com.leecoder.data.token

import android.util.Log
import com.leecoder.network.api.TokenApi
import com.leecoder.network.entity.TokenRequest
import com.leecoder.network.entity.toData
import com.leecoder.network.util.NetworkResult
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.model.token.TokenData
import com.leecoder.stockchart.model.token.TokenError
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRepositoryImpl @Inject constructor(
    private val tokenApi: TokenApi,
    private val dataStoreRepository: DataStoreRepository,
): TokenRepository {

    override suspend fun postToken(
        grantType: String,
        appsecret: String,
        appkey: String
    ): Pair<Boolean, TokenError?> {
        val reqeust = TokenRequest(grantType, appsecret, appkey)
        val response = tokenApi.postToken(reqeust)

        return try {
            if (response.isSuccessful) {
                val body = response.body() ?: return false to null
                Log.d("heesang", "${response.body()!!.toData()}")
                dataStoreRepository.refreshKrInvestmentToken(body.accessToken)
                dataStoreRepository.refreshKrInvestmentTokenExpired(body.tokenExpired)
                true to null
            }  else {
                throw Exception(response.errorBody()?.string())
            }
        } catch (e: Exception) {
            val errorBody = e.message?.let {
                Json.decodeFromString<TokenError>(it)
            } ?: TokenError()

            Log.i("heesang", "[Exception] -> ${response.errorBody()}")
            false to TokenError(errorBody.errorDescription, errorBody.errorCode)
        }
    }
}