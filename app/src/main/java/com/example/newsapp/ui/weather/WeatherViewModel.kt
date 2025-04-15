package com.example.newsapp.ui.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.data.remote.NetworkHelper
import com.example.newsapp.data.remote.WeatherRepository
import com.example.newsapp.data.remote.response.WeatherResponse
import com.example.newsapp.ui.base.BaseViewModel
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {
    private val _weatherResponse = MutableLiveData<Resource<WeatherResponse>>()
    val weatherResponse: LiveData<Resource<WeatherResponse>> get() = _weatherResponse

    fun getCurrentWeatherData(city: String) {
        safeApiCall(_weatherResponse, networkHelper) {
            weatherRepository.getCurrentWeather(city)
        }
    }
}