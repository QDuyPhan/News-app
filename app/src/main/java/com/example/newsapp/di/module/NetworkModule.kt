package com.example.newsapp.di.module

import com.example.newsapp.BuildConfig
import com.example.newsapp.data.local.PreferenceRepository
import com.example.newsapp.data.remote.service.AuthService
import com.example.newsapp.data.remote.service.NewsService
import com.example.newsapp.utils.Constants.NETWORK_TIMEOUT
import com.example.newsapp.utils.LocalDateTimeDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import javax.inject.Provider
import javax.inject.Singleton


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
    fun provideGson(): Gson =
        GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
            .setLenient().create()

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        preferenceRepository: PreferenceRepository,
        authService: Provider<AuthService>
    ): AuthInterceptor {
        return AuthInterceptor(preferenceRepository, authService)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()

        builder.addInterceptor(authInterceptor)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
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

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }
}