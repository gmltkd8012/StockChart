package com.leecoder.data.token

import android.util.Log
import com.leecoder.network.api.TokenApi
import com.leecoder.network.entity.TokenRequest
import com.leecoder.network.entity.toData
import com.leecoder.network.util.NetworkResult
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.model.token.TokenData
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.Exception
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
    ): Boolean {
        val reqeust = TokenRequest(grantType, appsecret, appkey)
        val response = tokenApi.postToken(reqeust)

        return try {
            if (response.isSuccessful) {
                val body = response.body() ?: return false
                Log.d("heesang", "${response.body()!!.toData()}")
                dataStoreRepository.refreshKrInvestmentToken(body.accessToken)
                dataStoreRepository.refreshKrInvestmentTokenExpired(body.tokenExpired)
                true
            }  else {
                Log.e("heesang", "${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.i("heesang", "[Exception] -> ${response.errorBody()}")
            false
        }
    }
}