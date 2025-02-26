package com.example.newsapp

import android.app.Application
import android.content.Context
import com.example.newsapp.utils.Constants.PREFS_NAME
import com.example.newsapp.utils.Logger
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class NewsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}