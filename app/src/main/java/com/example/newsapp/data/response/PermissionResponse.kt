package com.example.newsapp.data.response

import com.google.gson.annotations.SerializedName

data class PermissionResponse(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String
)