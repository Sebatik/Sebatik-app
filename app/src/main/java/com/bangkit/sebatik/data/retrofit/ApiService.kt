package com.bangkit.sebatik.data.retrofit

import com.bangkit.sebatik.data.response.ProductResponseItem
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/products")
    suspend fun getProducts(): List<ProductResponseItem>

    @GET("/products")
    suspend fun getAllProducts(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): List<ProductResponseItem>
}