package com.example.newsapp.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.local.PreferenceRepository
import com.example.newsapp.data.remote.NetworkHelper
import com.example.newsapp.data.remote.NewsRepository
import com.example.newsapp.data.remote.request.LoginRequest
import com.example.newsapp.data.remote.request.LogoutRequest
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

    private val _logoutResult = MutableLiveData<Resource<ApiResponse<Void>>>()
    val logoutResult: LiveData<Resource<ApiResponse<Void>>> get() = _logoutResult

    private val _deleteUserResult = MutableLiveData<Resource<ApiResponse<String>>>()
    val deleteUserResult: LiveData<Resource<ApiResponse<String>>> get() = _deleteUserResult

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
        safeApiCall(_loginResult, networkHelper) {
            newsRepository.login(LoginRequest(username, password))
        }
    }

    fun logout(token: String) {
        safeApiCall(_logoutResult, networkHelper) {
            newsRepository.logout(LogoutRequest(token))
        }
    }

    fun signup(name: String, username: String, password: String, email: String) {
        safeApiCall(_signupResult, networkHelper) {
            newsRepository.signup(SignupRequest(name, username, email, password))
        }
    }

    fun deleteUser(userId: String) {
        safeApiCall(_deleteUserResult, networkHelper) {
            newsRepository.deleteUser(userId)
        }

    }
}