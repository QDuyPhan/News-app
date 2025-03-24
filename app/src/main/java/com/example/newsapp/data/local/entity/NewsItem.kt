package com.example.newsapp.data.local.entity

import com.example.newsapp.data.remote.response.NewsResponse

data class NewsItem(
    val news: NewsResponse,
    var isSelected: Boolean = false
)
