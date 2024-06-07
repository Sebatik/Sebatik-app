package com.bangkit.sebatik.data.retrofit

import com.bangkit.sebatik.data.response.ScanResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ScanApiService {

    @Multipart
    @POST("predict")
    suspend fun predict(
        @Part file: MultipartBody.Part
    ) : ScanResponse
}