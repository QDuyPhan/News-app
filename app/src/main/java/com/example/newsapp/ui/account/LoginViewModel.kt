package com.example.newsapp.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.remote.NewsRepository
import com.example.newsapp.data.remote.request.LoginRequest
import com.example.newsapp.data.remote.response.ApiResponse
import com.example.newsapp.data.remote.response.AuthenticationResponse
import com.example.newsapp.ui.base.BaseViewModel
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val newsRepository: NewsRepository) :
    BaseViewModel() {
    private val _loginResult = MutableLiveData<Resource<ApiResponse<AuthenticationResponse>>>()
    val loginResult: LiveData<Resource<ApiResponse<AuthenticationResponse>>> get() = _loginResult

    fun login(username: String, password: String) = viewModelScope.launch(exceptionHandler) {
//        callLogin(LoginRequest(username, password)).enqueue(object :
//            Callback<ApiResponse<AuthenticationResponse>> {
//            override fun onResponse(
//                call: Call<ApiResponse<AuthenticationResponse>>,
//                response: Response<ApiResponse<AuthenticationResponse>>
//            ) {
//                if (response.isSuccessful) {
//                    response.body()?.let {
//                        _loginResult.value = Resource.Success(it)
//                    }
//                } else {
//                    _loginResult.value = Resource.Error("Login failed", null)
//                }
//            }
//
//            override fun onFailure(
//                call: Call<ApiResponse<AuthenticationResponse>>,
//                throwable: Throwable
//            ) {
//                _loginResult.value = Resource.Error("Network error", null)
//            }
//
//        })
//        registerEventParentJobFinish()
    }

    private suspend fun callLogin(loginRequest: LoginRequest): Call<ApiResponse<AuthenticationResponse>> {
        return newsRepository.login(loginRequest)
    }

}