package com.example.newsapp.di.module

import android.content.Context
import com.example.newsapp.BuildConfig
import com.example.newsapp.data.local.datastore.AppSettingImpl
import com.example.newsapp.data.remote.service.AuthService
import com.example.newsapp.data.remote.service.NewsService
import com.example.newsapp.utils.AuthAuthenticator
import com.example.newsapp.utils.AuthInterceptor
import com.example.newsapp.utils.Constants.NETWORK_TIMEOUT
import com.example.newsapp.utils.LocalDateTimeDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import javax.inject.Named
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    @Named("news_base_url")
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideConnectionTimeout() = NETWORK_TIMEOUT

    @Provides
    @Singleton
    @Named("default_gson")
    fun provideGson(): Gson =
        GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
            .setLenient().create()

    @Singleton
    @Provides
    fun provideAuthInterceptor(appSettingImpl: AppSettingImpl): AuthInterceptor =
        AuthInterceptor(appSettingImpl)

    @Singleton
    @Provides
    fun provideAuthAuthenticator(
        appSettingImpl: AppSettingImpl,
        authService: Provider<AuthService>
    ): AuthAuthenticator =
        AuthAuthenticator(appSettingImpl = appSettingImpl, authService = authService)

    @Singleton
    @Provides
    fun provideAppSettingImpl(@ApplicationContext context: Context): AppSettingImpl =
        AppSettingImpl(context)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authAuthenticator: AuthAuthenticator,
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("news_base_url") baseUrl: String,
        @Named("default_gson") gson: Gson,
        okHttpClient: OkHttpClient
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