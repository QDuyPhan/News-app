package com.example.newsapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class AuthenticationResponse(
    @SerializedName("authenticated") val authenticated: Boolean,
    @SerializedName("token") val token: String,
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: Set<String>
)