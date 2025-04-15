package com.example.newsapp.ui.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.newsapp.data.remote.response.WeatherResponse
import com.example.newsapp.databinding.FragmentWeatherBinding
import com.example.newsapp.ui.base.BaseFragment
import com.example.newsapp.utils.Constants.ICON_WEATHER
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.setOnSingClickListener
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class WeatherFragment : BaseFragment<FragmentWeatherBinding>() {
    private val weatherViewModel by viewModels<WeatherViewModel>()
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWeatherBinding {
        return FragmentWeatherBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        collectData()
    }

    private fun initUI() {
        with(binding) {
            btnSearch.setOnClickListener {
                val city = binding.edittextSearch.text.toString()
                weatherViewModel.getCurrentWeatherData(if (city.isEmpty()) "saigon" else city)
            }

            btnBack.setOnSingClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun collectData() {
        observeResource(
            liveData = weatherViewModel.weatherResponse,
            onSuccess = {
                updateUI(it)
            },
            onError = {
                val error = it
                Logger.logE("WeatherFragment: $error")
            },
            onLoading = {
                Logger.logE("WeatherFragment: Loading")
            }
        )

        weatherViewModel.getCurrentWeatherData("saigon")
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(weatherResponse: WeatherResponse) {
        binding.apply {
            textviewThanhpho.text = weatherResponse.name

            val date = Date(weatherResponse.dt * 1000L)
            val simpleDateFormat =
                SimpleDateFormat("EEEE yyyy-MM-dd \n           HH-mm-ss", Locale.getDefault())
            val day = simpleDateFormat.format(date)
            textviewNgayCapNhat.text = day

            val weather = weatherResponse.weather.firstOrNull()
            weather?.let {
                Picasso.get().load(ICON_WEATHER + it.icon + ".png").into(imgIcon)
                textviewTrangThai.text = it.main
            }

            val main = weatherResponse.main
            val Nhietdo = "${(main.temp.toInt() - 273)}°C"
            textviewNhietDo.text = Nhietdo
            textviewDoam.text = "${main.humidity}%"

            val wind = weatherResponse.wind
            textviewGio.text = "${wind.speed} m/s"

            val clouds = weatherResponse.clouds
            textviewMay.text = "${clouds.all}%"

            val sys = weatherResponse.sys
            textviewQuocGia.text = "Tên quốc gia : ${sys.country}"
        }
    }

}