package com.example.newsapp.ui.splash

import com.example.newsapp.data.local.PreferenceRepository
import com.example.newsapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val preferenceRepository: PreferenceRepository) :
    BaseViewModel() {
}