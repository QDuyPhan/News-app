package com.example.newsapp.data.remote.request

import com.google.gson.annotations.SerializedName

data class SignupRequest(
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
)