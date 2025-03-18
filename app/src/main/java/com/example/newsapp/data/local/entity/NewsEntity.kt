package com.example.newsapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapp.data.remote.response.CategoryResponse
import java.io.Serializable
import java.time.LocalDateTime

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey
    val newsId: Long,
    val title: String,
    val content: String,
    val image: String,
    val publishedAt: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val categories: Set<CategoryResponse>,
    val userId: String
) : Serializable
