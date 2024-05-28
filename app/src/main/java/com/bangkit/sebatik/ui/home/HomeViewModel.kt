package com.bangkit.sebatik.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.sebatik.data.Result
import com.bangkit.sebatik.data.repository.Repository
import com.bangkit.sebatik.data.response.ExploreResponse
import com.bangkit.sebatik.data.response.ExploreResponseItem

class HomeViewModel(private val repository: Repository) : ViewModel() {
    // TODO: Implement the ViewModel
    fun getPhoto(): LiveData<Result<List<ExploreResponseItem>>> = repository.getPhotos()

    fun getProducts(): LiveData<Result<List<ExploreResponseItem>>> = repository.getProducts()

}