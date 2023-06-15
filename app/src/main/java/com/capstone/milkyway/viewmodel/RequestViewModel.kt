package com.capstone.milkyway.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.milkyway.api.ApiConfig
import com.capstone.milkyway.response.ResponseAddDonor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestViewModel : ViewModel() {
    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun addRequest(
        token: String,
        userId: String,
        name: String,
        age: Int,
        phone: Int,
        religion: String,
        healthCondition: String,
        isSmoking: String,
        bloodType: String,
        dietary: String,
        address: String,
        role: String,
    ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService()
            .add(
                bearer = "Bearer $token",
                userId = userId,
                name = name,
                age = age,
                phone = phone,
                religion = religion,
                healthCondition = healthCondition,
                isSmoking = isSmoking,
                bloodType = bloodType,
                dietary = dietary,
                address = address,
                role = role
            )
        client.enqueue(object : Callback<ResponseAddDonor> {
            override fun onResponse(
                call: Call<ResponseAddDonor>,
                response: Response<ResponseAddDonor>
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

            override fun onFailure(call: Call<ResponseAddDonor>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message
            }
        })
    }
}