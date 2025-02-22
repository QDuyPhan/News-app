package com.example.newsapp.data.response

import com.google.gson.annotations.SerializedName

data class AuthenticationResponse(
    @SerializedName("authenticated") val authenticated: Boolean,
    @SerializedName("token") val token: String,
)