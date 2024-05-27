package com.bangkit.sebatik.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userPref")

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>){

    fun getLogin(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IS_LOGIN] ?: false
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[KEY_TOKEN] ?: ""
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_TOKEN] = token
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[IS_LOGIN] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[IS_LOGIN] = false
            preferences[KEY_TOKEN] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val KEY_TOKEN = stringPreferencesKey("token")
        private val IS_LOGIN = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}