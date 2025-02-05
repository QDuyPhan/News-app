package com.example.newsapp.data.remote.response

data class AuthenticationResponse(
    val authenticated: Boolean,
    val token: String,
    val id: Long,
    val name: String,
    val username: String,
    val email: String,
    val role: Set<String>
)