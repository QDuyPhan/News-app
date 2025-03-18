package com.example.newsapp.ui.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.local.LocalRepository
import com.example.newsapp.data.local.datastore.AppSettingImpl
import com.example.newsapp.data.local.entity.NewsEntity
import com.example.newsapp.data.remote.NetworkHelper
import com.example.newsapp.data.remote.NewsRepository
import com.example.newsapp.data.remote.response.UserResponse
import com.example.newsapp.ui.base.BaseViewModel
import com.example.newsapp.utils.IODispatcher
import com.example.newsapp.utils.MainDispatcher
import com.example.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val networkHelper: NetworkHelper,
    private val localRepository: LocalRepository,
    @IODispatcher private val IOdispatcher: CoroutineDispatcher,
    @MainDispatcher private val Maindispatcher: CoroutineDispatcher,
    private val appSettingImpl: AppSettingImpl,
) : BaseViewModel() {

    private val _newsSaved = MutableLiveData<Resource<List<NewsEntity>>>()
    val newsSaved: LiveData<Resource<List<NewsEntity>>> get() = _newsSaved

    val user = MutableLiveData<UserResponse?>()

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch(IOdispatcher + exceptionHandler) {
            appSettingImpl.getUserInfo().collect {
                withContext(Maindispatcher) {
                    user.value = it
                }
            }
        }
    }

    fun getSavedNews(userId: String) {
        viewModelScope.launch(exceptionHandler + Maindispatcher) {
            _newsSaved.postValue(Resource.loading(null))
            val response = localRepository.getAllNews(userId = userId)
            if (response.isNotEmpty()) {
                _newsSaved.postValue(Resource.success(response))
            } else _newsSaved.postValue(Resource.error("No data", null))
        }
    }

}