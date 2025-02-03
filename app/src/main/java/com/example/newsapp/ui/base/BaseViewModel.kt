package com.example.newsapp.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.utils.Logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job

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
        Logger.log(throwable.message.toString())
    }
}