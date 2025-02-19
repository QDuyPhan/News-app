package com.example.newsapp.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.utils.Logger
import com.example.newsapp.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseViewModel : ViewModel() {
    protected var parentJob: Job? = null

    var isLoading = MutableLiveData(false)
        private set

    protected fun registerEventParentJobFinish() {
        parentJob?.invokeOnCompletion {
            isLoading.postValue(false)
        }
    }

    protected val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Logger.logE(throwable.message.toString())
    }
}