package com.example.newsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.data.local.entity.NewsEntity

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNews(newsEntity: NewsEntity)

    @Query("SELECT * FROM news")
    suspend fun getAllNews(): List<NewsEntity>

    @Query("DELETE FROM news")
    suspend fun deleteAllNews()
}