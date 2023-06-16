package com.capstone.milkyway.api

import com.capstone.milkyway.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("api/donors")
    fun getAllDonors(
        @Header("Authorization") bearer: String,
    ): Call<ResponseGetAllDonors>

    @FormUrlEncoded
    @POST("api/donors")
    fun add(
        @Header("Authorization") bearer: String,
        @Field("userId") userId: String,
        @Field("name") name: String,
        @Field("age") age: Int,
        @Field("phone") phone: Long,
        @Field("religion") religion: String,
        @Field("health_condition") healthCondition: String,
        @Field("is_smoke") isSmoking: String,
        @Field("blood_type") bloodType: String,
        @Field("dietary") dietary: String,
        @Field("address") address: String,
        @Field("role") role: String
    ): Call<ResponseAddDonor>

    @DELETE("api/donors/{uuid}")
    fun deleteDonor(
        @Header("Authorization") bearer: String,
        @Path("uuid") uuid: String
    ): Call<ResponseDeleteDonor>

    @FormUrlEncoded
    @PUT("api/donors/{uuid}")
    fun updateDonor(
        @Header("Authorization") bearer: String,
        @Path("uuid") uuid: String,
        @Field("name") name: String,
        @Field("phone") phone: Int,
        @Field("address") address: String
    ): Call<ResponseUpdateDonor>

    @FormUrlEncoded
    @POST("model/recommend")
    fun getAllRecommends(
        @Header("Authorization") bearer: String,
        @Field("Age") age: Int,
        @Field("Religion") religion: String,
        @Field("Health Condition") healthCondition: String,
        @Field("is_smoke") isSmoking: String,
        @Field("Blood Type") bloodType: String,
        @Field("Dietary Restrictions") dietary: String,
        @Field("Location") address: String,
    ): Call<List<ResponseRecommendItem>>
}