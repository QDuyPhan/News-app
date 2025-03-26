package com.example.newsapp.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.local.LocalRepository
import com.example.newsapp.data.local.datastore.AppSettingImpl
import com.example.newsapp.data.local.entity.NewsEntity
import com.example.newsapp.data.remote.NetworkHelper
import com.example.newsapp.data.remote.NewsRepository
import com.example.newsapp.data.remote.response.ApiResponse
import com.example.newsapp.data.remote.response.NewsResponse
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
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val networkHelper: NetworkHelper,
    private val localRepository: LocalRepository,
    @IODispatcher private val IOdispatcher: CoroutineDispatcher,
    @MainDispatcher private val Maindispatcher: CoroutineDispatcher,
    private val appSettingImpl: AppSettingImpl,
) : BaseViewModel() {

    private val _newsResult = MutableLiveData<Resource<ApiResponse<List<NewsResponse>>>>()
    val newsResult: LiveData<Resource<ApiResponse<List<NewsResponse>>>> get() = _newsResult

    private val _deleteResult = MutableLiveData<Resource<ApiResponse<Void>>>()
    val deleteResult: LiveData<Resource<ApiResponse<Void>>> get() = _deleteResult

    val user = MutableLiveData<UserResponse?>()

    init {
        getUserInfo()
    }

    fun getData(categoryName: String?) {
        safeApiCall(_newsResult, networkHelper) {
            newsRepository.getNewsByCategory((categoryName))
        }
    }

    fun saveReadNews(newsEntity: NewsEntity) {
        viewModelScope.launch(exceptionHandler + IOdispatcher) {
            localRepository.saveNews(newsEntity)
        }
    }

    fun deleteNews(id: Long) {
        safeApiCall(_deleteResult, networkHelper) {
            newsRepository.deleteNews(id)
        }

    }

    private fun getUserInfo() {
        viewModelScope.launch(IOdispatcher + exceptionHandler) {
            appSettingImpl.getUserInfo().collect {
                withContext(Maindispatcher) {
                    user.value = it
                }
            }
        }
    }

}