package com.bangkit.sebatik.data.retrofit

import com.bangkit.sebatik.data.response.BatikResponse
import com.bangkit.sebatik.data.response.DatasItem
import com.bangkit.sebatik.data.response.ScanResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @GET("/batik/all")
    suspend fun getBatik(
        @Header("Authorization") token: String,
    ) : BatikResponse
}