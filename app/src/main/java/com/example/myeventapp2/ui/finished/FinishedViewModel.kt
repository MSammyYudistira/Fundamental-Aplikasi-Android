package com.example.myeventapp2.ui.finished

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myeventapp2.data.response.ListEventResponse
import com.example.myeventapp2.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listEvent = MutableLiveData<ListEventResponse>()
    val listEvent: LiveData<ListEventResponse> = _listEvent

    companion object{
        private const val TAG = "FinishedViewModel"
        private const val ACTIVE = 0
    }

    fun findAllEvent(q: String ?= "") {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListEvents(ACTIVE,q)
        client.enqueue(object : Callback<ListEventResponse> {
            override fun onResponse(
                call: Call<ListEventResponse>,
                response: Response<ListEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listEvent.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ListEventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    init {
        findAllEvent()
    }
}