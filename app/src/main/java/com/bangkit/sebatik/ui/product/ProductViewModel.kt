package com.bangkit.sebatik.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.bangkit.sebatik.data.Result
import com.bangkit.sebatik.data.repository.Repository
import com.bangkit.sebatik.data.response.ProductResponseItem

class ProductViewModel(private val repository: Repository) : ViewModel() {

    fun getAllProducts(): LiveData<Result<PagingData<ProductResponseItem>>> = repository.getAllProducts()
}