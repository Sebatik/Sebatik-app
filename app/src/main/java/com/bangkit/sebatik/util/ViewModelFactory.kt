package com.bangkit.sebatik.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.data.di.Injection
import com.bangkit.sebatik.data.repository.Repository
import com.bangkit.sebatik.ui.addproduct.AddProductViewModel
import com.bangkit.sebatik.ui.home.HomeViewModel
import com.bangkit.sebatik.ui.login.LoginViewModel
import com.bangkit.sebatik.ui.product.ProductViewModel
import com.bangkit.sebatik.ui.register.RegisterViewModel
import com.bangkit.sebatik.ui.settings.SettingsViewModel

class ViewModelFactory private constructor(private val repository: Repository, private val pref: UserPreferences):
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(pref) as T
        } else if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(pref) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(AddProductViewModel::class.java)) {
            return AddProductViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }



    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context, pref: UserPreferences): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), pref)
            }.also { instance = it }
    }
}