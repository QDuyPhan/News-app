package com.example.newsapp.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.local.PreferenceRepository
import com.example.newsapp.data.remote.NetworkHelper
import com.example.newsapp.data.remote.NewsRepository
import com.example.newsapp.data.remote.request.LoginRequest
import com.example.newsapp.data.remote.request.SignupRequest
import com.example.newsapp.data.remote.response.ApiResponse
import com.example.newsapp.data.remote.response.AuthenticationResponse
import com.example.newsapp.data.remote.response.UserResponse
import com.example.newsapp.ui.base.BaseViewModel
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

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

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()


    init {
        viewModelScope.launch(exceptionHandler + Dispatchers.Main) {
            _isReady.value = true
        }
    }

    fun isLogin(): Boolean {
        return preferenceRepository.isUserLoggedIn()
    }

    fun saveLoginState(value: Boolean) {
        preferenceRepository.saveUserLoginStatus(value)
    }


    fun login(username: String, password: String) {
        viewModelScope.launch(exceptionHandler) {
            _loginResult.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val response = newsRepository.login(LoginRequest(username, password))
                response.let {
                    if (response.isSuccessful) {
                        _loginResult.postValue(Resource.success(it.body()))
                    } else _loginResult.postValue(Resource.error(it.message().toString(), null))
                }
            } else _loginResult.postValue(Resource.error("No internet connection", null))
        }
        registerEventParentJobFinish()
    }

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


}