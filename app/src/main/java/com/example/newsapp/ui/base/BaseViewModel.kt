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
        Logger.log(throwable.message.toString())
    }

    fun <T> performAction(
        resultLiveData: MutableLiveData<Resource<T>>,
        apiCall: () -> Call<T>
    ) {
        resultLiveData.value = Resource.Loading()
        val call = apiCall()
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    responseData?.let {
                        resultLiveData.value = Resource.Success(it)
                    }
                } else {
                    resultLiveData.value = Resource.Error("Request failed")
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                Logger.logD("Network error")
                resultLiveData.value = Resource.Error("Network error")
            }
        })
    }
}