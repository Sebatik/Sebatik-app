package com.bangkit.sebatik.data.response

import com.google.gson.annotations.SerializedName

data class ScanResponse(

	@field:SerializedName("batikDesc")
	val batikDesc: String? = null,

	@field:SerializedName("class_index")
	val classIndex: Int? = null,

	@field:SerializedName("batikName")
	val batikName: String? = null
)
