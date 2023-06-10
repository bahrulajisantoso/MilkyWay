package com.capstone.milkyway.response

import com.google.gson.annotations.SerializedName

data class ResponseGetAllDonors(

	@field:SerializedName("status_code")
	val statusCode: Int,

	@field:SerializedName("payload")
	val payload: List<PayloadItem>,

	@field:SerializedName("message")
	val message: String
)

data class PayloadItem(

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("uuid")
	val uuid: String,

	@field:SerializedName("health_condition")
	val healthCondition: String,

	@field:SerializedName("religion")
	val religion: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("dietary")
	val dietary: String,

	@field:SerializedName("insertedAt")
	val insertedAt: String,

	@field:SerializedName("is_smoke")
	val isSmoke: String,

	@field:SerializedName("blood_type")
	val bloodType: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("age")
	val age: Int,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
