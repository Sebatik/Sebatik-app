package com.bangkit.sebatik.data.response

import com.google.gson.annotations.SerializedName

data class ExploreResponse(

	@field:SerializedName("ExploreResponse")
	val exploreResponse: List<ExploreResponseItem> = emptyList()
)

data class ExploreResponseItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("price")
	val price: Any? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("category")
	val category: String? = null
)
