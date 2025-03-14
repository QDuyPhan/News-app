package com.example.newsapp.data.remote

import com.example.newsapp.data.remote.request.LoginRequest
import com.example.newsapp.data.remote.request.SignupRequest
import com.example.newsapp.data.remote.response.ApiResponse
import com.example.newsapp.data.remote.response.AuthenticationResponse
import com.example.newsapp.data.remote.response.CategoryResponse
import com.example.newsapp.data.remote.response.NewsResponse
import com.example.newsapp.data.remote.response.UserResponse
import com.example.newsapp.data.remote.service.NewsService
import com.example.newsapp.utils.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsService: NewsService,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun login(loginRequest: LoginRequest): Response<ApiResponse<AuthenticationResponse>> =
        withContext(dispatcher)
        {
            newsService.login(loginRequest)
        }

    fun signup(signupRequest: SignupRequest): Call<ApiResponse<UserResponse>> =
        newsService.signup(signupRequest)

    suspend fun getCategories(): Response<ApiResponse<List<CategoryResponse>>> =
        withContext(dispatcher)
        {
            newsService.getCategories()
        }

    suspend fun getNewsByCategory(categoryName: String?): Response<ApiResponse<List<NewsResponse>>> =
        withContext(dispatcher)
        {
            newsService.getNewsByCategory(categoryName)
        }

    suspend fun getMyInfo(): Response<ApiResponse<UserResponse>> =
        withContext(dispatcher) {
            newsService.getMyInfo()
        }

    suspend fun getAllNews(): Response<ApiResponse<List<NewsResponse>>> =
        withContext(dispatcher)
        {
            newsService.getAllNews()
        }
}