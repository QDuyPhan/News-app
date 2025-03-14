package com.example.newsapp.ui.summary

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
class SummaryViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {
    private val _news = MutableLiveData<Resource<ApiResponse<List<NewsResponse>>>>()
    val news: LiveData<Resource<ApiResponse<List<NewsResponse>>>> get() = _news

    init {
        getAllNews()
    }

    private fun getAllNews() {
        viewModelScope.launch(exceptionHandler) {
            _news.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val response = newsRepository.getAllNews()
                response.let {
                    if (response.isSuccessful) {
                        _news.postValue(Resource.success(it.body()))
                    } else {
                        _news.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }
            } else _news.postValue(Resource.error("No internet connection", null))
        }
    }
}