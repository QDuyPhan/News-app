package com.example.newsapp.data.remote

import com.example.newsapp.utils.Constants.API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class NetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("API_KEY", API_KEY)
            .build()
        return chain.proceed(request)
    }
}