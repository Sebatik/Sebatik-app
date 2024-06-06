package com.bangkit.sebatik.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.bangkit.sebatik.data.response.BatikResponse
import com.bangkit.sebatik.data.retrofit.ApiService
import com.bangkit.sebatik.data.Result
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.data.response.DatasItem
import com.bangkit.sebatik.data.retrofit.ApiConfig
import com.bangkit.sebatik.data.source.ProductPagingSource
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class Repository private constructor(
    private val apiService: ApiService,
    private val pref: UserPreferences
) {


    fun getAllBatik() : LiveData<Result<PagingData<DatasItem>>> = liveData {
        emit(Result.Loading)
        try {
            val pager = Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
                pagingSourceFactory = {
                    ProductPagingSource(apiService, pref)
                }
            ).liveData
            val pagingData = pager.asFlow().first()
            emit(Result.Success(pagingData))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody().toString()
            val error = Gson().fromJson(jsonInString, BatikResponse::class.java)
            emit(Result.Error(error.toString()))
        }
    }

    fun getAnotherBatik(): LiveData<Result<List<DatasItem>>> = liveData {
        emit(Result.Loading)
        try {
            val token = runBlocking {
                pref.getToken().first()
            }
            val response = ApiConfig.getApiService().getBatik("Bearer $token")
            val stories = response.payload.datas
            emit(Result.Success(stories))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(jsonInString, BatikResponse::class.java)
            emit(Result.Error(error.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            pref: UserPreferences
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, pref)
            }.also { instance = it }
    }

}