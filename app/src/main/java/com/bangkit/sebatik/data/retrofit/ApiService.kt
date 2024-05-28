package com.bangkit.sebatik.data.retrofit

import com.bangkit.sebatik.data.response.ExploreResponse
import com.bangkit.sebatik.data.response.ExploreResponseItem
import retrofit2.http.GET

interface ApiService {

    @GET("/products")
    suspend fun getProducts(): List<ExploreResponseItem>
}