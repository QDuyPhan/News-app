package com.example.newsapp.data.remote.request

data class UpdateUserRequest(
    val name: String,
    val email: String,
    val password: String,
)
