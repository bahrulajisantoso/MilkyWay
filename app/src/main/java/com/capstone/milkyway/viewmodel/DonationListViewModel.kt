package com.capstone.milkyway.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.milkyway.api.ApiConfig
import com.capstone.milkyway.response.PayloadItem
import com.capstone.milkyway.response.ResponseDeleteDonor
import com.capstone.milkyway.response.ResponseGetAllDonors
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DonationListViewModel : ViewModel() {

    private val _donors = MutableLiveData<List<PayloadItem>>()
    val donors: LiveData<List<PayloadItem>> = _donors

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

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
                    _error.value = false
                    _donors.value = responseBody.payload
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage =
                        JSONObject(errorBody.toString()).getString("error").toString()
                    _error.value = true
                    _message.value = errorMessage
                }
            }

            override fun onFailure(call: Call<ResponseGetAllDonors>, t: Throwable) {
                _error.value = true
                _message.value = t.message
                _isLoading.value = false
            }
        })
    }

    fun deleteDonor(token: String, uuId: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService()
            .deleteDonor(bearer = "Bearer $token", uuId)
        client.enqueue(object : Callback<ResponseDeleteDonor> {
            override fun onResponse(
                call: Call<ResponseDeleteDonor>,
                response: Response<ResponseDeleteDonor>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _error.value = false
                    _message.value = responseBody.message
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage =
                        JSONObject(errorBody.toString()).getString("error").toString()
                    _error.value = true
                    _message.value = errorMessage
                }
            }

            override fun onFailure(call: Call<ResponseDeleteDonor>, t: Throwable) {
                _error.value = true
                _message.value = t.message
                _isLoading.value = false
            }
        })
    }

    companion object {
        private val TAG = DonationListViewModel::class.java.simpleName
    }
}