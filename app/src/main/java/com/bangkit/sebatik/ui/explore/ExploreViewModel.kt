package com.bangkit.sebatik.ui.explore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.bangkit.sebatik.data.Result
import com.bangkit.sebatik.data.repository.Repository
import com.bangkit.sebatik.data.response.DatasItem

class ExploreViewModel(private val repository: Repository) : ViewModel() {
    fun getAllBatik(): LiveData<Result<PagingData<DatasItem>>> = repository.getAllBatik()

    fun getBatik(): LiveData<Result<List<DatasItem>>> = repository.getAnotherBatik()
}