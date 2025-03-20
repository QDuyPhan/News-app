package com.example.newsapp.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.remote.NetworkHelper
import kotlinx.coroutines.launch
import retrofit2.Response

inline fun <T> LifecycleOwner.observeResource(
    liveData: LiveData<Resource<T>>,
    crossinline onSuccess: (T) -> Unit,
    crossinline onError: (String) -> Unit = {},
    crossinline onLoading: () -> Unit = {}
) {
    liveData.observe(this) { resource ->
        when (resource.status) {
            Status.SUCCESS -> resource.data?.let { onSuccess(it) }
            Status.ERROR -> onError(resource.message ?: "Unknown error")
            Status.LOADING -> onLoading()
        }
    }
}

inline fun <T> ViewModel.safeApiCall(
    liveData: MutableLiveData<Resource<T>>,
    networkHelper: NetworkHelper,
    crossinline apiCall: suspend () -> Response<T>
) {
    viewModelScope.launch {
        liveData.postValue(Resource.loading(null))
        if (networkHelper.isNetworkConnected()) {
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    liveData.postValue(Resource.success(response.body()))
                } else {
                    liveData.postValue(Resource.error(response.message().toString(), null))
                }
            } catch (e: Exception) {
                liveData.postValue(Resource.error(e.localizedMessage ?: "Unknown error", null))
            }
        } else {
            liveData.postValue(Resource.error("No internet connection", null))
        }
    }
}
