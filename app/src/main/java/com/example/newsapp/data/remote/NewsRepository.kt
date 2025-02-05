package com.example.newsapp.data.remote

import com.example.newsapp.data.remote.request.LoginRequest
import com.example.newsapp.data.remote.response.ApiResponse
import com.example.newsapp.data.remote.response.AuthenticationResponse
import com.example.newsapp.data.remote.service.NewsService
import com.example.newsapp.utils.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Call
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsService: NewsService,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun login(loginRequest: LoginRequest): Call<ApiResponse<AuthenticationResponse>> =
        withContext(dispatcher) {
            newsService.login(loginRequest)
        }
}