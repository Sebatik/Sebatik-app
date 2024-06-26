package com.bangkit.sebatik.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.sebatik.data.models.Product
import com.bangkit.sebatik.data.models.User
import com.bangkit.sebatik.data.repository.Repository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private lateinit var firebaseAuth: FirebaseAuth
    private val firebaseRef = FirebaseDatabase.getInstance().getReference("products")
    private lateinit var productList: MutableList<Product>

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _product  = MutableLiveData<List<Product>>()
    val product: LiveData<List<Product>> = _product

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    fun fetchUsername() {
        firebaseAuth = Firebase.auth
        val currentUser = firebaseAuth.currentUser
        currentUser?.let {
            val userId = currentUser.uid
            val databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    _username.value = user?.username ?: ""
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("HomeFragment", "onCancelled: ${error.message}")
                }
            })
        }
    }

    fun loadProducts() {
        isLoading.value = true
        productList = mutableListOf()
        firebaseRef.limitToLast(5).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                    productList.clear()
                    if (snapshot.exists()) {
                        for (productSnapshot in snapshot.children) {
                            isLoading.value = false
                            val product = productSnapshot.getValue(Product::class.java)
                            productList.add(product!!)
                        }
                        _product.value = productList.reversed()
                    }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeFragment", "onCancelled: ${error.message}")
            }
        })
    }
}