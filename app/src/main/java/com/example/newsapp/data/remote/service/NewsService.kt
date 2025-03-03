package com.example.newsapp.data.remote.service

import com.example.newsapp.data.request.LoginRequest
import com.example.newsapp.data.request.SignupRequest
import com.example.newsapp.data.response.ApiResponse
import com.example.newsapp.data.response.AuthenticationResponse
import com.example.newsapp.data.response.CategoryResponse
import com.example.newsapp.data.response.UserResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NewsService {
    @POST("/api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<ApiResponse<AuthenticationResponse>>

    @POST("/api/users")
    fun signup(
        @Body signupRequest: SignupRequest
    ): Call<ApiResponse<UserResponse>>

    @GET("/api/category")
    suspend fun getCategories(): Response<ApiResponse<List<CategoryResponse>>>
}