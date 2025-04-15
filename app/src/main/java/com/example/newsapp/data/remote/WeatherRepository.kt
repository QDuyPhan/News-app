package com.example.newsapp.data.remote

import com.example.newsapp.data.remote.response.WeatherResponse
import com.example.newsapp.data.remote.service.WeatherApiService
import com.example.newsapp.utils.Constants.API_WEATHER_KEY
import com.example.newsapp.utils.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val apiService: WeatherApiService,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun getCurrentWeather(
        city: String,
    ): Response<WeatherResponse> =
        withContext(dispatcher)
        {
            apiService.getCurrentWeather(city, API_WEATHER_KEY)
        }
}