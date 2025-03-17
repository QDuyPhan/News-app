package com.example.newsapp.utils

import com.example.newsapp.data.local.datastore.AppSettingImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val appSettingImpl: AppSettingImpl,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            appSettingImpl.getToken().first()
        }
        val originalRequest = chain.request()
        if (originalRequest.url.encodedPath.contains("/api/auth/login") ||
            originalRequest.url.encodedPath.contains("/api/users") ||
            originalRequest.url.encodedPath.contains("/api/auth/refresh")
        ) {
            return chain.proceed(originalRequest)
        }
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}