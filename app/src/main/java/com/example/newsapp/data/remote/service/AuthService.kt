package com.example.newsapp.data.remote.service

import com.example.newsapp.data.remote.request.RefreshRequest
import com.example.newsapp.data.remote.response.ApiResponse
import com.example.newsapp.data.remote.response.RefreshResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/api/auth/refresh")
    suspend fun refreshToken(@Body refreshRequest: RefreshRequest): Response<ApiResponse<RefreshResponse>>
}