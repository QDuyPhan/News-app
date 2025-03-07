package com.example.newsapp.ui.articles

import androidx.lifecycle.ViewModel
import com.example.newsapp.data.remote.NetworkHelper
import com.example.newsapp.data.remote.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val networkHelper: NetworkHelper
) :
    ViewModel() {
}