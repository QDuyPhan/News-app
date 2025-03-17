package com.example.newsapp.ui.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.local.datastore.AppSettingImpl
import com.example.newsapp.ui.base.BaseViewModel
import com.example.newsapp.utils.IODispatcher
import com.example.newsapp.utils.MainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TokenViewModel @Inject constructor(
    private val appSettingImpl: AppSettingImpl,
    @IODispatcher private val IOdispatcher: CoroutineDispatcher,
    @MainDispatcher private val Maindispatcher: CoroutineDispatcher,
) :
    BaseViewModel() {
    val token = MutableLiveData<String?>()

    init {
        viewModelScope.launch(IOdispatcher) {
            appSettingImpl.getToken().collect {
                withContext(Maindispatcher) {
                    token.value = it
                }
            }
        }
    }

    fun saveToken(token: String) {
        viewModelScope.launch(IOdispatcher) {
            appSettingImpl.saveToken(token)
        }
    }

    fun deleteToken() {
        viewModelScope.launch(IOdispatcher) {
            appSettingImpl.deleteToken()
        }
    }
}