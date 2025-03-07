package com.example.newsapp.data.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.LocalDateTime

data class NewsResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("image") val image: String,
    @SerializedName("publishedAt") val publishedAt: LocalDateTime,
    @SerializedName("created_at") val createdAt: LocalDateTime,
    @SerializedName("updated_at") val updatedAt: LocalDateTime,
    @SerializedName("categories") val categories: Set<CategoryResponse>
) : Serializable
