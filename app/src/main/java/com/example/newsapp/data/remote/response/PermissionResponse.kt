package com.example.newsapp.data.remote.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PermissionResponse(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String
)