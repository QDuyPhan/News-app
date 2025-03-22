package com.example.newsapp.ui.postnews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.data.remote.NetworkHelper
import com.example.newsapp.data.remote.NewsRepository
import com.example.newsapp.data.remote.request.PostNewsRequest
import com.example.newsapp.data.remote.response.ApiResponse
import com.example.newsapp.data.remote.response.NewsResponse
import com.example.newsapp.ui.base.BaseViewModel
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class PostNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository, private val networkHelper: NetworkHelper
) : BaseViewModel() {
    private val _postNewsResponse = MutableLiveData<Resource<ApiResponse<NewsResponse>>>()
    val postNewsResponse: LiveData<Resource<ApiResponse<NewsResponse>>> get() = _postNewsResponse

    private val publishedAt = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    private val created_at = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    private val updated_at = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    fun postNews(title: String, content: String, image: String, category: String) {
        safeApiCall(_postNewsResponse, networkHelper) {
            newsRepository.createPostNews(
                PostNewsRequest(
                    title, content, image, publishedAt, created_at, updated_at, setOf(category)
                )
            )
        }
    }
}