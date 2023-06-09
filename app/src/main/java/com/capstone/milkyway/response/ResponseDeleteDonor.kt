package com.capstone.milkyway.response

import com.google.gson.annotations.SerializedName

data class ResponseDeleteDonor(

	@field:SerializedName("status_code")
	val statusCode: Int,

	@field:SerializedName("payload")
	val payload: Int,

	@field:SerializedName("message")
	val message: String
)
