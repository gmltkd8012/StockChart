package com.leecoder.data.token

import com.leecoder.data.source.AuthDataSource
import com.leecoder.data.source.TokenException
import com.leecoder.network.const.Credential
import com.leecoder.stockchart.datastore.repository.DataStoreRepository
import com.leecoder.stockchart.model.token.TokenError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val dataStoreRepository: DataStoreRepository,
) : AuthRepository {

    override suspend fun checkLogin(
        appkey: String,
        appsecret: String,
    ): Flow<Boolean> = flow {
        val result = authDataSource.postToken(
            grantType = Credential.CLIENT_CREDENTIAL,
            appsecret = appsecret,
            appkey = appkey
        )

        emit(
            result.fold(
                onSuccess = { tokenData ->
                    dataStoreRepository.refreshKrInvestmentToken(tokenData.tokenType + " " + tokenData.accessToken)
                    dataStoreRepository.refreshKrInvestmentTokenExpired(tokenData.tokenExpired)
                    dataStoreRepository.saveAppKey(appkey)
                    dataStoreRepository.saveAppSecret(appsecret)
                    true
                },
                onFailure = {
                    false
                }
            )
        )
    }

    override suspend fun postToken(
        grantType: String,
        appsecret: String,
        appkey: String
    ): Pair<Boolean, TokenError?> {
        val result = authDataSource.postToken(grantType, appsecret, appkey)

        return result.fold(
            onSuccess = { tokenData ->
                dataStoreRepository.refreshKrInvestmentToken(tokenData.tokenType + " " + tokenData.accessToken)
                dataStoreRepository.refreshKrInvestmentTokenExpired(tokenData.tokenExpired)
                true to null
            },
            onFailure = { exception ->
                if (exception is TokenException) {
                    false to exception.tokenError
                } else {
                    false to TokenError(errorDescription = exception.message ?: "Unknown error")
                }
            }
        )
    }
}