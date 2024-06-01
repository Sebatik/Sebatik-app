package com.bangkit.sebatik.ui.product

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.bangkit.sebatik.data.Result
import com.bangkit.sebatik.data.adapter.AllProductAdapter
import com.bangkit.sebatik.data.models.Product
import com.bangkit.sebatik.data.repository.Repository
import com.bangkit.sebatik.data.response.ProductResponseItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductViewModel(private val repository: Repository) : ViewModel() {


}