package com.bangkit.sebatik.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.sebatik.data.Result
import com.bangkit.sebatik.data.repository.Repository
import com.bangkit.sebatik.data.response.ProductResponseItem

class HomeViewModel(private val repository: Repository) : ViewModel() {

    fun getProducts(): LiveData<Result<List<ProductResponseItem>>> = repository.getProducts()

}