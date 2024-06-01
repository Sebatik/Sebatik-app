package com.bangkit.sebatik.ui.home

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.sebatik.data.Result
import com.bangkit.sebatik.data.adapter.ProductAdapter
import com.bangkit.sebatik.data.models.Product
import com.bangkit.sebatik.data.models.User
import com.bangkit.sebatik.data.repository.Repository
import com.bangkit.sebatik.data.response.ProductResponseItem
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private lateinit var firebaseAuth: FirebaseAuth

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username
    
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
}