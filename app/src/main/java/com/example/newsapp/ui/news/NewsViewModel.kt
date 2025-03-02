package com.example.newsapp.ui.news

import androidx.lifecycle.ViewModel
import com.example.newsapp.data.remote.NewsRepository
import com.example.newsapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val newsRepository: NewsRepository) :
    BaseViewModel() {
}