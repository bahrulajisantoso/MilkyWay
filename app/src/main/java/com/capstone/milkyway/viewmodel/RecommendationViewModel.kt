package com.capstone.milkyway.viewmodel

import android.util.Log
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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private val TAG = RecommendationViewModel::class.java.simpleName
    }

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
                    _recommends.value = response.body()
                    Log.d(TAG, response.body().toString())
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ResponseRecommendItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}
