package com.example.newsapp.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.newsapp.data.remote.response.UserResponse
import com.example.newsapp.utils.Constants.DATA_STORE
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppSettingImpl(private val context: Context) : AppSetting {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE)
    private val gson = Gson()
    override suspend fun getToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[AppSettingDatastoreKeys.TOKEN_KEY]
        }
    }

    override suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[AppSettingDatastoreKeys.TOKEN_KEY] = token
        }
    }

    override suspend fun deleteToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(AppSettingDatastoreKeys.TOKEN_KEY)
        }
    }

    override suspend fun saveUserInfo(
        userResponse: UserResponse
    ) {
        val jsonString = gson.toJson(userResponse)
        context.dataStore.edit { preferences ->
            preferences[AppSettingDatastoreKeys.USER_KEY] = jsonString
        }
    }

    override suspend fun deleteUserInfo() {
        context.dataStore.edit { preferences ->
            preferences.remove(AppSettingDatastoreKeys.USER_KEY)
        }
    }

    override suspend fun getUserInfo(): Flow<UserResponse?> {
        return context.dataStore.data.map { preferences ->
            preferences[AppSettingDatastoreKeys.USER_KEY]?.let { jsonString ->
                gson.fromJson(jsonString, UserResponse::class.java)
            }
        }
    }


}