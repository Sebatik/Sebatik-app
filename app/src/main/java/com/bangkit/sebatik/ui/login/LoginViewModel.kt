package com.bangkit.sebatik.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.sebatik.data.UserPreferences
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: UserPreferences) : ViewModel() {
    // TODO: Implement the ViewModel

    fun isLogin(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
            pref.login()
        }
    }
}