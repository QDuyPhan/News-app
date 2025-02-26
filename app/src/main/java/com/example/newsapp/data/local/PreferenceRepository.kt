package com.example.newsapp.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.newsapp.data.response.RoleResponse
import com.example.newsapp.utils.Constants.EMAIL
import com.example.newsapp.utils.Constants.ID
import com.example.newsapp.utils.Constants.NAME
import com.example.newsapp.utils.Constants.PREFS_NAME
import com.example.newsapp.utils.Constants.ROLE
import com.example.newsapp.utils.Constants.TOKEN
import com.example.newsapp.utils.Constants.USERNAME
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) {
    fun saveTokenKey(token: String) {
        sharedPreferences.edit().putString(TOKEN, token).apply()
    }

    fun getID(): String? = sharedPreferences.getString(ID, "")

    fun getName(): String? = sharedPreferences.getString(NAME, "")

    fun getUsername(): String? = sharedPreferences.getString(USERNAME, "")

    fun getEmail(): String? = sharedPreferences.getString(EMAIL, "")

    fun getRole(): Set<RoleResponse>? =
        sharedPreferences.getStringSet(ROLE, setOf()) as Set<RoleResponse>?

    fun getTokenKey(): String? = sharedPreferences.getString(TOKEN, "")

}