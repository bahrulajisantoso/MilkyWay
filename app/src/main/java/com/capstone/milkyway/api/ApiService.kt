package com.capstone.milkyway.api

import com.capstone.milkyway.response.ResponseAddDonor
import com.capstone.milkyway.response.ResponseGetAllDonors
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("donors")
    fun getAllDonors(
        @Header("Authorization") bearer: String,
    ): Call<ResponseGetAllDonors>

    @FormUrlEncoded
    @POST("v1/add")
    fun addDonor(
        @Field("userId") userId: String,
        @Field("name") name: String,
        @Field("age") age: String,
        @Field("religion") religion: String,
        @Field("dietary") dietary: String,
        @Field("address") address: String,
        @Field("role") role: String
    ): Call<ResponseAddDonor>
}