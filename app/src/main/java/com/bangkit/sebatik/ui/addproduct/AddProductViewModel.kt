package com.bangkit.sebatik.ui.addproduct

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.sebatik.data.models.User
import com.bangkit.sebatik.data.repository.Repository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.File

class AddProductViewModel(private val repository: Repository) : ViewModel() {

    private lateinit var firebaseAuth: FirebaseAuth
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> = _phoneNumber
    fun fetchProfile() {
        firebaseAuth = Firebase.auth
        val currentUser = firebaseAuth.currentUser
        currentUser?.let {
            val userId = currentUser.uid
            val databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    _username.value = user?.username ?: ""
                    _phoneNumber.value = user?.phoneNumber ?: ""
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("HomeFragment", "onCancelled: ${error.message}")
                }

            })
        }
    }

    fun scanBatik(file: File?) = repository.scanBatik(file)

}