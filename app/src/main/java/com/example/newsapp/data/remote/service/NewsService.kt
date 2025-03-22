package com.example.newsapp.data.remote.service

import com.example.newsapp.data.remote.request.LoginRequest
import com.example.newsapp.data.remote.request.PostNewsRequest
import com.example.newsapp.data.remote.request.SignupRequest
import com.example.newsapp.data.remote.response.ApiResponse
import com.example.newsapp.data.remote.response.AuthenticationResponse
import com.example.newsapp.data.remote.response.CategoryResponse
import com.example.newsapp.data.remote.response.NewsResponse
import com.example.newsapp.data.remote.response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NewsService {
    @POST("/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<ApiResponse<AuthenticationResponse>>

    @POST("/users")
    suspend fun signup(
        @Body signupRequest: SignupRequest
    ): Response<ApiResponse<UserResponse>>

    @GET("/category")
    suspend fun getCategories(): Response<ApiResponse<List<CategoryResponse>>>

    @GET("/news/{categoryName}")
    suspend fun getNewsByCategory(@Path("categoryName") categoryName: String?):
            Response<ApiResponse<List<NewsResponse>>>

    @GET("/users/myInfo")
    suspend fun getMyInfo(): Response<ApiResponse<UserResponse>>

    @GET("/news")
    suspend fun getAllNews(): Response<ApiResponse<List<NewsResponse>>>

    @POST("/news")
    suspend fun createPostNews(@Body postNewsRequest: PostNewsRequest): Response<ApiResponse<NewsResponse>>
}