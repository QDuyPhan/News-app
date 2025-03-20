package com.example.newsapp.data.local.entity

import kotlinx.serialization.Serializable

data class Role(
    val name: String,
    val description: String,
    val permissions: Set<Permission>
)