package com.example.newsapp.data.local.entity

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

data class User(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val password: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val roles: Set<Role>
)