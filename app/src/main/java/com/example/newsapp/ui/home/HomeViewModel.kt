package com.example.newsapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.local.datastore.AppSettingImpl
import com.example.newsapp.data.remote.NetworkHelper
import com.example.newsapp.data.remote.NewsRepository
import com.example.newsapp.data.remote.response.ApiResponse
import com.example.newsapp.data.remote.response.CategoryResponse
import com.example.newsapp.data.remote.response.UserResponse
import com.example.newsapp.ui.base.BaseViewModel
import com.example.newsapp.utils.IODispatcher
import com.example.newsapp.utils.MainDispatcher
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val networkHelper: NetworkHelper,
    private val appSettingImpl: AppSettingImpl,
    @IODispatcher private val IOdispatcher: CoroutineDispatcher,
    @MainDispatcher private val Maindispatcher: CoroutineDispatcher,
) : BaseViewModel() {
    private val _listCategory = MutableLiveData<Resource<ApiResponse<List<CategoryResponse>>>>()
    val listCategory: LiveData<Resource<ApiResponse<List<CategoryResponse>>>> get() = _listCategory

    private val _userInfo = MutableLiveData<Resource<ApiResponse<UserResponse>>>()
    val userInfo: LiveData<Resource<ApiResponse<UserResponse>>> get() = _userInfo


    init {
        getCategories()
        getUserInfo()
    }

    fun saveUserInfo(userResponse: UserResponse) {
        viewModelScope.launch(IOdispatcher) {
            appSettingImpl.saveUserInfo(userResponse)
        }
    }

    fun deleteUserInfo() {
        viewModelScope.launch(IOdispatcher) {
            appSettingImpl.deleteUserInfo()
        }
    }


    private fun getUserInfo() {
        viewModelScope.launch(exceptionHandler) {
            _userInfo.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val response = newsRepository.getMyInfo()
                response.let {
                    if (response.isSuccessful) {
                        _userInfo.postValue(Resource.success(it.body()))
                    } else _userInfo.postValue(Resource.error(it.message().toString(), null))
                }
            } else _userInfo.postValue(Resource.error("No internet connection", null))
        }
    }

    private fun getCategories() {
        viewModelScope.launch(exceptionHandler) {
            _listCategory.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val response = newsRepository.getCategories()
                response.let {
                    if (response.isSuccessful) {
                        _listCategory.postValue(Resource.success(it.body()))
                    } else _listCategory.postValue(
                        Resource.error(
                            it.message().toString(),
                            null
                        )
                    )
                }
            } else _listCategory.postValue(Resource.error("No internet connection", null))
        }
    }


}