package com.example.newsapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.remote.NetworkHelper
import com.example.newsapp.data.remote.NewsRepository
import com.example.newsapp.data.response.ApiResponse
import com.example.newsapp.data.response.CategoryResponse
import com.example.newsapp.ui.base.BaseViewModel
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val networkHelper: NetworkHelper
) : BaseViewModel() {
    private val _listCategory = MutableLiveData<Resource<ApiResponse<List<CategoryResponse>>>>()
    val listCategory: LiveData<Resource<ApiResponse<List<CategoryResponse>>>> get() = _listCategory

    init {
        getCategories()
    }

    private fun getCategories() {
        viewModelScope.launch(exceptionHandler) {
            _listCategory.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val response = newsRepository.getCategories()
                response.let {
                    if (response.isSuccessful) {
                        _listCategory.postValue(Resource.success(it.body()))
                    } else _listCategory.postValue(Resource.error(it.message().toString(), null))
                }
            } else _listCategory.postValue(Resource.error("No internet connection", null))
        }
    }
}