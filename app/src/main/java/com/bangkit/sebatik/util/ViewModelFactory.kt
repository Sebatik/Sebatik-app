package com.bangkit.sebatik.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.ui.login.LoginViewModel
import com.bangkit.sebatik.ui.register.RegisterViewModel

class ViewModelFactory private constructor(private val pref: UserPreferences):
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }



    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context, pref: UserPreferences): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(pref)
            }.also { instance = it }
    }
}