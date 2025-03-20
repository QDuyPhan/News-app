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
        Logger.logI("AuthInterceptor: $token")
        val originalRequest = chain.request()
        if (originalRequest.url.encodedPath.contains("/auth/login") ||
            originalRequest.url.encodedPath.contains("/users") ||
            originalRequest.url.encodedPath.contains("/auth/refresh") ||
            originalRequest.url.encodedPath.contains("/auth/logout")
        ) {
            return chain.proceed(originalRequest)
        }
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}