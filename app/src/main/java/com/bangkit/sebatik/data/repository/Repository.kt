package com.bangkit.sebatik.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.bangkit.sebatik.data.Result
import com.bangkit.sebatik.data.response.ProductResponseItem
import com.bangkit.sebatik.data.retrofit.ApiService
import com.bangkit.sebatik.data.source.ProductPagingSource
import kotlinx.coroutines.flow.first

class Repository private constructor(private val apiService: ApiService) {

    fun getProducts(): LiveData<Result<List<ProductResponseItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getProducts()
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllProducts() : LiveData<Result<PagingData<ProductResponseItem>>> = liveData {
        emit(Result.Loading)
        try {
            val pager = Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
                pagingSourceFactory = {
                    ProductPagingSource(apiService)
                }
            ).liveData
            val pagingData = pager.asFlow().first()
            emit(Result.Success(pagingData))
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