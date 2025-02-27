package com.example.newsapp.di.module

import com.example.newsapp.BuildConfig
import com.example.newsapp.data.local.PreferenceRepository
import com.example.newsapp.data.remote.service.NewsService
import com.example.newsapp.utils.Constants.DEFAULT_TIMEOUT
import com.example.newsapp.utils.Constants.NETWORK_TIMEOUT
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideConnectionTimeout() = NETWORK_TIMEOUT

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(preferenceRepository: PreferenceRepository): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val headerInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val newRequest = originalRequest
                .newBuilder()
//                .addHeader(
//                    "Authorization", "Bearer ${preferenceRepository.getTokenKey()}"
//                )
                .build()
            chain.proceed(newRequest)
        }
        builder.addInterceptor(headerInterceptor)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
            builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            builder.retryOnConnectionFailure(true)
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        baseUrl: String, gson: Gson, okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }


    @Provides
    @Singleton
    fun provideNewsService(retrofit: Retrofit): NewsService =
        retrofit.create(NewsService::class.java)

}