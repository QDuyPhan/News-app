package com.example.newsapp.data.response

import com.google.gson.annotations.SerializedName

data class RefreshResponse(
    @SerializedName("authenticated") val authenticated: Boolean,
    @SerializedName("token") val token: String
)