package com.bangkit.sebatik.data.retrofit

import com.bangkit.sebatik.data.response.BatikResponse
import com.bangkit.sebatik.data.response.DatasItem
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET("/batik/all")
    suspend fun getBatik(
        @Header("Authorization") token: String,
    ) : BatikResponse
}