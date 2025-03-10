package com.example.newsapp.di.module

import com.example.newsapp.data.local.PreferenceRepository
import com.example.newsapp.data.remote.service.AuthService
import com.example.newsapp.data.request.RefreshRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Provider

class AuthInterceptor @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val authService: Provider<AuthService>
) : Interceptor {
    override fun intercept(chain: Chain): Response {
        val token = preferenceRepository.getTokenKey()
        val originalRequest = chain.request()
        if (originalRequest.url.encodedPath.contains("/api/auth/login") ||
            originalRequest.url.encodedPath.contains("/api/users")
        ) {
            return chain.proceed(originalRequest)
        }
        val request = chain.request()
        if (!token.isNullOrEmpty()) {
            request.newBuilder().addHeader("Authorization", "Bearer $token").build()
            val response = chain.proceed(request = request)
            return if (response.code == 401) {
                response.close()
                refreshToken(chain, request, token)
            } else {
                response
            }
        } else {
            return refreshToken(chain, request, token!!)
        }
    }

    private fun refreshToken(chain: Chain, request: Request, tokenOld: String): Response {
        val response = runBlocking {
            authService.get().refreshToken(RefreshRequest(tokenOld))
        }
        return if (response.isSuccessful) {
            val token = response.body()?.result?.token
            preferenceRepository.saveTokenKey(token!!)
            val newRequest =
                request.newBuilder().addHeader("Authorization", "Bearer $token").build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(request)
        }
    }
}