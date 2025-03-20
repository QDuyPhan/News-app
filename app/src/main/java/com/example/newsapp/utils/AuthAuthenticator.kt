package com.example.newsapp.utils

import com.example.newsapp.data.local.datastore.AppSettingImpl
import com.example.newsapp.data.remote.request.RefreshTokenRequest
import com.example.newsapp.data.remote.response.ApiResponse
import com.example.newsapp.data.remote.response.RefreshTokenResponse
import com.example.newsapp.data.remote.service.AuthService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class AuthAuthenticator @Inject constructor(
    private val appSettingImpl: AppSettingImpl, private val authService: Provider<AuthService>
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val token = runBlocking {
            appSettingImpl.getToken().first()
        }
        Logger.logI("AuthAuthenticator: $token")
        return runBlocking {
            val newToken = getNewToken(token)

            if (!newToken.isSuccessful || newToken.body() == null) {
                appSettingImpl.deleteToken()
            }

            newToken.body()?.let {
                appSettingImpl.saveToken(it.result.token)
                response.request.newBuilder().header("Authorization", "Bearer ${it.result.token}")
                    .build()
            }
        }
    }

    private suspend fun getNewToken(refreshToken: String?): retrofit2.Response<ApiResponse<RefreshTokenResponse>> {
        return authService.get().refreshToken(RefreshTokenRequest(refreshToken.toString()))
    }

}