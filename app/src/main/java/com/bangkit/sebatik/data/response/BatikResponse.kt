package com.bangkit.sebatik.data.response

import com.google.gson.annotations.SerializedName

data class BatikResponse(

	@field:SerializedName("payload")
	val payload: Payload,

	@field:SerializedName("message")
	val message: String
)

data class DatasItem(

	@field:SerializedName("batikDesc")
	val batikDesc: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("batikImg")
	val batikImg: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("batikName")
	val batikName: String
)

data class Payload(

	@field:SerializedName("status_code")
	val statusCode: Int,

	@field:SerializedName("datas")
	val datas: List<DatasItem>
)
