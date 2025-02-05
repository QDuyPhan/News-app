package com.example.newsapp.data.remote.response

data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T
)
