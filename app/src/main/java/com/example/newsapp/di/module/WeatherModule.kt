package com.example.newsapp.di.module

import com.example.newsapp.data.remote.service.WeatherApiService
import com.example.newsapp.utils.Constants.BASE_URL_WEATHER
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {

    @Provides
    @Singleton
    @Named("weather_base_url")
    fun provideWeatherBaseUrl() = BASE_URL_WEATHER

    @Provides
    @Singleton
    @Named("weather_gson")
    fun provideWeatherGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    @Named("weather")
    fun provideWeatherRetrofit(
        @Named("weather_base_url") baseUrl: String,
        @Named("weather_gson") gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApi(@Named("weather") retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }
}