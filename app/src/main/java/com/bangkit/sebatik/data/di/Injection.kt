package com.bangkit.sebatik.data.di

import android.content.Context
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

//object Injection {
//    fun provideRepository(context: Context): Repository {
//        val pref = UserPreferences.getInstance(context.dataStore)
//        val apiService = ApiConfig.getApiService()
//        return Repository.getInstance(apiService, pref)
//    }
//}