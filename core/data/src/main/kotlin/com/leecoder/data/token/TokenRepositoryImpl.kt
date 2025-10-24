package com.leecoder.data.token

import android.util.Log
import com.leecoder.network.api.TokenApi
import com.leecoder.network.entity.TokenRequest
import com.leecoder.network.entity.toData
import com.leecoder.network.util.NetworkResult
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
): TokenRepository {

    override suspend fun postToken(
        grantType: String,
        appsecret: String,
        appkey: String
    ) {
        val reqeust = TokenRequest(grantType, appsecret, appkey)
        Log.d("heesang", "$reqeust")

        val response = tokenApi.postToken(reqeust)

        try {
            if (response.isSuccessful) {
                Log.d("heesang", "${response.body()!!.toData()}")
            }  else {
                Log.e("heesang", "${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.i("heesang", "[Exception] -> ${response.errorBody()}")
        }





    }
}