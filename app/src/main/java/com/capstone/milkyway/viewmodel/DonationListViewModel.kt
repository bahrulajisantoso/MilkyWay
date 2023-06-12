package com.capstone.milkyway.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.milkyway.api.ApiConfig
import com.capstone.milkyway.response.PayloadItem
import com.capstone.milkyway.response.ResponseDelete
import com.capstone.milkyway.response.ResponseGetAllDonors
import com.capstone.milkyway.response.ResponseUpdateDonor
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

    fun deleteDonor(token: String, uuId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService()
            .deleteDonor(bearer = "Bearer $token", uuId)
        client.enqueue(object : Callback<ResponseDelete> {
            override fun onResponse(
                call: Call<ResponseDelete>,
                response: Response<ResponseDelete>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    Log.d(TAG, responseBody.message)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseDelete>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun updateDonor(token: String, uuId: String, name: String, phone: String, address: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService()
            .updateDonor(bearer = "Bearer $token", uuId, name, phone, address)
        client.enqueue(object : Callback<ResponseUpdateDonor> {
            override fun onResponse(
                call: Call<ResponseUpdateDonor>,
                response: Response<ResponseUpdateDonor>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    Log.d(TAG, responseBody.message)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseUpdateDonor>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}