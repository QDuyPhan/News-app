package com.example.newsapp.data.local

import android.content.SharedPreferences
import com.example.newsapp.data.remote.response.RoleResponse
import com.example.newsapp.utils.Constants.EMAIL
import com.example.newsapp.utils.Constants.ID
import com.example.newsapp.utils.Constants.IS_LOGIN
import com.example.newsapp.utils.Constants.NAME
import com.example.newsapp.utils.Constants.ROLE
import com.example.newsapp.utils.Constants.TOKEN
import com.example.newsapp.utils.Constants.UNACTIVATE
import com.example.newsapp.utils.Constants.USERNAME
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) {

    fun getID(): String? = sharedPreferences.getString(ID, "")

    fun getName(): String? = sharedPreferences.getString(NAME, "")

    fun getUsername(): String? = sharedPreferences.getString(USERNAME, "")

    fun getEmail(): String? = sharedPreferences.getString(EMAIL, "")

    fun getRole(): Set<RoleResponse>? =
        sharedPreferences.getStringSet(ROLE, setOf()) as Set<RoleResponse>?

    fun saveTokenKey(token: String) {
        sharedPreferences.edit().putString(TOKEN, token).apply()
    }

    fun getTokenKey(): String? = sharedPreferences.getString(TOKEN, null)

    fun saveUserLoginStatus(value: String) {
        sharedPreferences.edit().putString(IS_LOGIN, value).apply()
    }

    fun isUserLoggedIn(): String? {
        return sharedPreferences.getString(IS_LOGIN, UNACTIVATE)
    }

}