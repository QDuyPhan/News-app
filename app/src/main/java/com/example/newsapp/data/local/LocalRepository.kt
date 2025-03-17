package com.example.newsapp.data.local

import com.example.newsapp.data.local.entity.NewsEntity
import com.example.newsapp.data.local.service.LocalService
import com.example.newsapp.utils.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(
    private val localService: LocalService,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun saveNews(newsEntity: NewsEntity) = withContext(dispatcher) {
        localService.saveNews(newsEntity)
    }

    suspend fun getAllNews(): List<NewsEntity> = withContext(dispatcher) {
        localService.getAllNews()
    }

    suspend fun deleteAllNews() = withContext(dispatcher) {
        localService.deleteAllNews()
    }
}