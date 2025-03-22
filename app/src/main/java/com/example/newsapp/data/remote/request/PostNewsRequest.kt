package com.example.newsapp.data.remote.request

data class PostNewsRequest(
    val title: String,
    val content: String,
    val image: String,
    val publishedAt: String,
    val created_at: String,
    val updated_at: String,
    val categories: Set<String>,
)