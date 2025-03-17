package com.example.newsapp.data.local.service

import com.example.newsapp.data.local.NewsDao
import com.example.newsapp.data.local.entity.NewsEntity
import javax.inject.Inject

class LocalService @Inject constructor(private val newsDao: NewsDao) {
    suspend fun saveNews(newsEntity: NewsEntity) {
        newsDao.saveNews(newsEntity)
    }

    suspend fun getAllNews(): List<NewsEntity> {
        return newsDao.getAllNews()
    }

    suspend fun deleteAllNews() {
        newsDao.deleteAllNews()
    }
}