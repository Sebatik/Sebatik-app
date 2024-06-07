package com.bangkit.sebatik.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.sebatik.data.Result
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.data.response.BatikResponse
import com.bangkit.sebatik.data.response.DatasItem
import com.bangkit.sebatik.data.response.ScanResponse
import com.bangkit.sebatik.data.retrofit.ApiService
import com.bangkit.sebatik.data.retrofit.ScanApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File

class Repository private constructor(
    private val apiService: ApiService,
    private val scanApiService: ScanApiService,
    private val pref: UserPreferences
) {

    fun getBatik(): LiveData<Result<List<DatasItem>>> = liveData {
        emit(Result.Loading)
        try {
            val token = runBlocking {
                pref.getToken().first()
            }
            val response = apiService.getBatik("Bearer $token")
            val stories = response.payload.datas
            emit(Result.Success(stories))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(jsonInString, BatikResponse::class.java)
            emit(Result.Error(error.message))
        }
    }

    fun scanBatik(file: File?): LiveData<Result<ScanResponse>> = liveData {
        emit(Result.Loading)
        try {
            if (file != null) {
                val imageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
                val imageMultipart = MultipartBody.Part.createFormData(
                    "file",
                    file.name,
                    imageFile
                )
                val response = scanApiService.predict(imageMultipart)
                emit(Result.Success(response))
            } else {
                emit(Result.Error("File not found"))
            }
        } catch (e: HttpException) {
            emit(Result.Error(e.message().toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            scanApiService: ScanApiService,
            pref: UserPreferences
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, scanApiService, pref)
            }.also { instance = it }
    }

}