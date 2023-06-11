package com.capstone.milkyway.api

import com.capstone.milkyway.response.ResponseAddDonor
import com.capstone.milkyway.response.ResponseDelete
import com.capstone.milkyway.response.ResponseGetAllDonors
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("donors")
    fun getAllDonors(
        @Header("Authorization") bearer: String,
    ): Call<ResponseGetAllDonors>

    @FormUrlEncoded
    @POST("donors")
    fun addDonor(
        @Header("Authorization") bearer: String,
        @Field("userId") userId: String,
        @Field("name") name: String,
        @Field("age") age: Int,
        @Field("phone") phone: String,
        @Field("religion") religion: String,
        @Field("health_condition") healthCondition: String,
        @Field("is_smoke") isSmoking: String,
        @Field("blood_type") bloodType: String,
        @Field("dietary") dietary: String,
        @Field("address") address: String,
        @Field("role") role: String
    ): Call<ResponseAddDonor>

    @POST("donors{uuid}")
    fun deleteDonor(
        @Header("Authorization") bearer: String,
        @Path("uuid") uuid: String
    ): Call<ResponseDelete>
}