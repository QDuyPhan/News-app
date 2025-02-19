package com.example.newsapp.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.remote.NetworkHelper
import com.example.newsapp.data.remote.NewsRepository
import com.example.newsapp.data.remote.request.SignupRequest
import com.example.newsapp.data.remote.response.ApiResponse
import com.example.newsapp.data.remote.response.UserResponse
import com.example.newsapp.ui.base.BaseViewModel
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val networkHelper: NetworkHelper
) :
    BaseViewModel() {
    private val _signupResult = MutableLiveData<Resource<ApiResponse<UserResponse>>>()
    val signupResult: LiveData<Resource<ApiResponse<UserResponse>>> get() = _signupResult

    fun signup(name: String, username: String, password: String, email: String) {
        if (networkHelper.isNetworkConnected()) {
            _signupResult.value = Resource.loading(null)
            val call = newsRepository.signup(SignupRequest(name, username, email, password))
            call.enqueue(object : Callback<ApiResponse<UserResponse>> {
                override fun onResponse(
                    call: Call<ApiResponse<UserResponse>>,
                    response: Response<ApiResponse<UserResponse>>
                ) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        _signupResult.value = Resource.success(apiResponse)
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