package com.example.newsapp.data.local.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object AppSettingDatastoreKeys {
    val TOKEN_KEY = stringPreferencesKey("jwt_token")
    val USER_KEY = stringPreferencesKey("user_data")
}