package com.example.newsapp.data.local

import com.example.newsapp.utils.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(
    private val newsDao: NewsDao,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) {

}