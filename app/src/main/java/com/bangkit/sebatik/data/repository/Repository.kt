package com.bangkit.sebatik.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.sebatik.data.Result
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.data.response.ExploreResponse
import com.bangkit.sebatik.data.response.ExploreResponseItem
import com.bangkit.sebatik.data.retrofit.ApiConfig
import com.bangkit.sebatik.data.retrofit.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class Repository private constructor(private val apiService: ApiService) {

    fun getPhotos(): LiveData<Result<List<ExploreResponseItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getPhoto()
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getProducts(): LiveData<Result<List<ExploreResponseItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getProducts()
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService)
            }.also { instance = it }
    }

}