package com.example.newsapp.data.remote.service

import com.example.newsapp.data.remote.request.LogoutRequest
import com.example.newsapp.data.remote.request.RefreshTokenRequest
import com.example.newsapp.data.remote.response.ApiResponse
import com.example.newsapp.data.remote.response.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {
    @POST("/auth/refresh")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Response<ApiResponse<RefreshTokenResponse>>

    @POST("/auth/logout")
    suspend fun logout(@Body logoutRequest: LogoutRequest): Response<ApiResponse<Void>>

    @DELETE("/users/{userId}")
    suspend fun deleteUser(@Path("userId") userId: String): Response<ApiResponse<String>>
}