package com.bangkit.sebatik.ui.scan

import androidx.lifecycle.ViewModel
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.data.repository.Repository
import java.io.File

class ScanViewModel(private val repository: Repository) : ViewModel() {
    // TODO: Implement the ViewModel

    fun scanBatik(file: File?) = repository.scanBatik(file)
}