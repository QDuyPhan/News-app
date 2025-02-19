package com.example.newsapp.data.remote.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class UserResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("created_at") val createdAt: LocalDateTime,
    @SerializedName("updated_at") val updatedAt: LocalDateTime,
    @SerializedName("roles") val roles: Set<RoleResponse>
)