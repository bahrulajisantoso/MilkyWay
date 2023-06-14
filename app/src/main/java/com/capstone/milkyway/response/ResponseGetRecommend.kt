package com.capstone.milkyway.response

import com.google.gson.annotations.SerializedName

data class ResponseRecommendItem(

	@field:SerializedName("Blood type")
	val bloodType: Int,

	@field:SerializedName("is_smoke")
	val isSmoke: Int,

	@field:SerializedName("Religion")
	val religion: Int,

	@field:SerializedName("Dietary Restrictions")
	val dietaryRestrictions: Int,

	@field:SerializedName("Age")
	val age: Int,

	@field:SerializedName("Health Condition")
	val healthCondition: Int,

	@field:SerializedName("Location")
	val location: String
)
