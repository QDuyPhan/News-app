package com.example.newsapp.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.data.local.PreferenceRepository
import com.example.newsapp.data.remote.NetworkHelper
import com.example.newsapp.data.remote.NewsRepository
import com.example.newsapp.data.request.LoginRequest
import com.example.newsapp.data.response.ApiResponse
import com.example.newsapp.data.response.AuthenticationResponse
import com.example.newsapp.ui.base.BaseViewModel
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val networkHelper: NetworkHelper,
    private val preferenceRepository: PreferenceRepository
) : BaseViewModel() {
    private val _loginResult = MutableLiveData<Resource<ApiResponse<AuthenticationResponse>>>()
    val loginResult: LiveData<Resource<ApiResponse<AuthenticationResponse>>> get() = _loginResult

    fun login(username: String, password: String) {
        _loginResult.value = Resource.loading(null)
        if (networkHelper.isNetworkConnected()) {
            val call = newsRepository.login(LoginRequest(username, password))
            call.enqueue(object : Callback<ApiResponse<AuthenticationResponse>> {
                override fun onResponse(
                    call: Call<ApiResponse<AuthenticationResponse>>,
                    response: Response<ApiResponse<AuthenticationResponse>>
                ) {
                    val responseData = response.body()
                    if (response.isSuccessful) {
                        responseData?.let {
                            Logger.logI("Response Data: ${it.result}")
                            _loginResult.value = Resource.success(it)
                            preferenceRepository.saveTokenKey(it.result.token)
                        }
                    } else {
                        _loginResult.value = Resource.error(response.message(), null)
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<AuthenticationResponse>>, t: Throwable
                ) {
                    _loginResult.value = Resource.error(t.message.toString(), null)
                }
            })
        } else {
            _loginResult.value = Resource.error("No internet connection", null)
        }
    }

}