package com.capstone.milkyway.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.milkyway.api.ApiConfig
import com.capstone.milkyway.response.ResponseRecommendItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendationViewModel : ViewModel() {

    private val _recommends = MutableLiveData<List<ResponseRecommendItem>>()
    val recommends: LiveData<List<ResponseRecommendItem>> = _recommends

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllRecommend(
        token: String,
        age: Int,
        dietary: String,
        religion: String,
        health: String,
        smoking: String,
        blood: String,
        location: String
    ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService()
            .getAllRecommends(
                bearer = "Bearer $token",
                age = age,
                dietary = dietary,
                religion = religion,
                healthCondition = health,
                isSmoking = smoking,
                bloodType = blood,
                address = location
            )
        client.enqueue(object : Callback<List<ResponseRecommendItem>> {
            override fun onResponse(
                call: Call<List<ResponseRecommendItem>>,
                response: Response<List<ResponseRecommendItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _error.value = false
                    _recommends.value = response.body()
                } else {
                    _error.value = true
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<List<ResponseRecommendItem>>, t: Throwable) {
                _error.value = true
                _isLoading.value = false
                _message.value = t.message
            }
        })
    }

    companion object {
        private val TAG = RecommendationViewModel::class.java.simpleName
    }
}
