package com.example.newsapp.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.local.PreferenceRepository
import com.example.newsapp.data.remote.NetworkHelper
import com.example.newsapp.data.remote.NewsRepository
import com.example.newsapp.data.request.LoginRequest
import com.example.newsapp.data.request.SignupRequest
import com.example.newsapp.data.response.ApiResponse
import com.example.newsapp.data.response.AuthenticationResponse
import com.example.newsapp.data.response.UserResponse
import com.example.newsapp.ui.base.BaseViewModel
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val networkHelper: NetworkHelper,
    private val preferenceRepository: PreferenceRepository
) : BaseViewModel() {
    private val _signupResult = MutableLiveData<Resource<ApiResponse<UserResponse>>>()
    val signupResult: LiveData<Resource<ApiResponse<UserResponse>>> get() = _signupResult

    private val _loginResult = MutableLiveData<Resource<ApiResponse<AuthenticationResponse>>>()
    val loginResult: LiveData<Resource<ApiResponse<AuthenticationResponse>>> get() = _loginResult

    fun signup(name: String, username: String, password: String, email: String) {
        _signupResult.value = Resource.loading(null)
        if (networkHelper.isNetworkConnected()) {
            val call = newsRepository.signup(SignupRequest(name, username, email, password))
            call.enqueue(object : Callback<ApiResponse<UserResponse>> {
                override fun onResponse(
                    call: Call<ApiResponse<UserResponse>>,
                    response: Response<ApiResponse<UserResponse>>
                ) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        Logger.logI("API Response: $apiResponse")
                        apiResponse?.let {
                            _signupResult.value = Resource.success(apiResponse)
                        }
                    } else {
                        val error = response.message() ?: "Unknown error"
                        _signupResult.value = Resource.error(error, null)
                    }
                }

                override fun onFailure(call: Call<ApiResponse<UserResponse>>, t: Throwable) {
                    _signupResult.value = Resource.error(t.message.toString(), null)
                }
            })
        } else {
            _signupResult.value = Resource.error("No internet connection", null)
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch(exceptionHandler) {
            _loginResult.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val response = newsRepository.login(LoginRequest(username, password))
                response.let {
                    if (response.isSuccessful) {
                        _loginResult.postValue(Resource.success(it.body()))
                        it.body()?.result?.token?.let { it1 -> preferenceRepository.saveTokenKey(it1) }
                    } else _loginResult.postValue(Resource.error(it.message().toString(), null))
                }
            } else _loginResult.postValue(Resource.error("No internet connection", null))
        }
    }
}