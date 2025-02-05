package com.example.newsapp.data.remote.service

import com.example.newsapp.data.remote.request.LoginRequest
import com.example.newsapp.data.remote.response.ApiResponse
import com.example.newsapp.data.remote.response.AuthenticationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NewsService {
    @POST("/api/auth/login")
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<ApiResponse<AuthenticationResponse>>
}