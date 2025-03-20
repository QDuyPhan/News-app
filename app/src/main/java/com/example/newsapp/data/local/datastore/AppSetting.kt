package com.example.newsapp.data.local.datastore

import com.example.newsapp.data.remote.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface AppSetting {
    suspend fun saveToken(token: String)
    suspend fun getToken(): Flow<String?>
    suspend fun deleteToken()

    suspend fun saveUserInfo(
        userResponse: UserResponse
    )

    suspend fun deleteUserInfo()

    suspend fun getUserInfo(): Flow<UserResponse?>

}
