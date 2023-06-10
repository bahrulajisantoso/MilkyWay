package com.capstone.milkyway.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.milkyway.api.ApiConfig
import com.capstone.milkyway.response.PayloadItem
import com.capstone.milkyway.response.ResponseGetAllDonors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DonationListViewModel : ViewModel() {

    private val _donors = MutableLiveData<List<PayloadItem>>()
    val donors: LiveData<List<PayloadItem>> = _donors

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private val TAG = DonationListViewModel::class.java.simpleName
    }


    fun getAllDonors(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService()
            .getAllDonors(bearer = "Bearer $token")
        client.enqueue(object : Callback<ResponseGetAllDonors> {
            override fun onResponse(
                call: Call<ResponseGetAllDonors>,
                response: Response<ResponseGetAllDonors>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _donors.value = responseBody.payload
                    Log.d(TAG, responseBody.payload.toString())
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseGetAllDonors>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}