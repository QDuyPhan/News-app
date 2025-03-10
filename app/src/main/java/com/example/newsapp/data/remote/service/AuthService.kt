package com.example.newsapp.data.remote.service

import com.example.newsapp.data.request.RefreshRequest
import com.example.newsapp.data.response.ApiResponse
import com.example.newsapp.data.response.RefreshResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/api/auth/refresh")
    suspend fun refreshToken(@Body refreshRequest: RefreshRequest): Response<ApiResponse<RefreshResponse>>
}