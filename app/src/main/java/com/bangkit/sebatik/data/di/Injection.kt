package com.bangkit.sebatik.data.di

import android.content.Context
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.data.dataStore
import com.bangkit.sebatik.data.repository.Repository
import com.bangkit.sebatik.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val scanApiService = ApiConfig.getResult()
        return Repository.getInstance(apiService, scanApiService ,pref)
    }
}