package com.example.newsapp.utils

import androidx.room.TypeConverter
import com.example.newsapp.data.remote.response.CategoryResponse
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let {
            LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }
    }

    private val gson = Gson()

    @TypeConverter
    fun fromCategorySet(categories: Set<CategoryResponse>?): String? {
        return gson.toJson(categories)
    }

    @TypeConverter
    fun toCategorySet(data: String?): Set<CategoryResponse>? {
        return data?.let {
            val type = object : TypeToken<Set<CategoryResponse>>() {}.type
            gson.fromJson(it, type)
        }
    }
}