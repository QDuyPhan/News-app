package com.example.newsapp.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.remote.NetworkHelper
import com.example.newsapp.data.remote.NewsRepository
import com.example.newsapp.data.remote.response.ApiResponse
import com.example.newsapp.data.remote.response.NewsResponse
import com.example.newsapp.ui.base.BaseViewModel
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val networkHelper: NetworkHelper
) :
    BaseViewModel() {

    private val _newsResult = MutableLiveData<Resource<ApiResponse<List<NewsResponse>>>>()
    val newsResult: LiveData<Resource<ApiResponse<List<NewsResponse>>>> get() = _newsResult

    fun getData(categoryName: String?) {
        viewModelScope.launch(exceptionHandler) {
            _newsResult.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val response = newsRepository.getNewsByCategory(categoryName)
                response.let {
                    if (response.isSuccessful) {
                        _newsResult.postValue(Resource.success(it.body()))
                    } else
                        _newsResult.postValue(Resource.error(it.message().toString(), null))
                }
            } else _newsResult.postValue(Resource.error("No internet connection", null))
        }
    }

    fun saveReadNews() {

    }
}