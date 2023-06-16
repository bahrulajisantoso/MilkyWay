package com.capstone.milkyway.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.milkyway.api.ApiConfig
import com.capstone.milkyway.response.ResponseAddDonor
import com.capstone.milkyway.response.ResponseUpdateDonor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DonationViewModel : ViewModel() {
    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun addDonor(
        token: String,
        userId: String,
        name: String,
        age: Int,
        phone: Long,
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
                _error.value = true
                _message.value = t.message
            }
        })
    }

    fun updateDonor(
        token: String,
        uuid: String,
        name: String,
        phone: Int,
        address: String,
    ) {
        _isLoading.value = true
        val client = ApiConfig.getApiService()
            .updateDonor(
                bearer = "Bearer $token",
                uuid = uuid,
                name = name,
                phone = phone,
                address = address,
            )
        client.enqueue(object : Callback<ResponseUpdateDonor> {
            override fun onResponse(
                call: Call<ResponseUpdateDonor>,
                response: Response<ResponseUpdateDonor>
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

            override fun onFailure(call: Call<ResponseUpdateDonor>, t: Throwable) {
                _isLoading.value = false
                _error.value = true
                _message.value = t.message
            }
        })
    }
}