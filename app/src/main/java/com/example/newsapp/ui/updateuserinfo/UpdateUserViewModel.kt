package com.example.newsapp.ui.updateuserinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.local.datastore.AppSettingImpl
import com.example.newsapp.data.remote.NetworkHelper
import com.example.newsapp.data.remote.NewsRepository
import com.example.newsapp.data.remote.request.UpdateUserRequest
import com.example.newsapp.data.remote.response.ApiResponse
import com.example.newsapp.data.remote.response.UserResponse
import com.example.newsapp.ui.base.BaseViewModel
import com.example.newsapp.utils.IODispatcher
import com.example.newsapp.utils.MainDispatcher
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UpdateUserViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val networkHelper: NetworkHelper,
    @IODispatcher private val IOdispatcher: CoroutineDispatcher,
    @MainDispatcher private val Maindispatcher: CoroutineDispatcher,
    private val appSettingImpl: AppSettingImpl,
) : BaseViewModel() {

    private val _updateUserResult = MutableLiveData<Resource<ApiResponse<UserResponse>>>()
    val updateUserResult: LiveData<Resource<ApiResponse<UserResponse>>> get() = _updateUserResult

    val userId = MutableLiveData<UserResponse>()

    init {
        getUserInfo()
    }

    fun updateUser(userId: String, name: String, email: String, password: String) {

        safeApiCall(_updateUserResult, networkHelper) {
            newsRepository.updateUser(
                userId,
                UpdateUserRequest(name, email, password)
            )
        }
    }

    fun saveUserInfo(userResponse: UserResponse) {
        viewModelScope.launch(IOdispatcher) {
            appSettingImpl.saveUserInfo(userResponse)
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch(IOdispatcher + exceptionHandler) {
            appSettingImpl.getUserInfo().collect {
                withContext(Maindispatcher) {
                    userId.value = it
                }
            }
        }
    }
}