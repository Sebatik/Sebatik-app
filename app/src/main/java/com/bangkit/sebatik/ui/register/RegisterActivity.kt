package com.bangkit.sebatik.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.sebatik.data.models.User
import com.bangkit.sebatik.databinding.ActivityRegisterBinding
import com.bangkit.sebatik.ui.login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference
        firebaseAuth = Firebase.auth

        binding.tvToSignin.setOnClickListener {toLogin()}
        binding.btnRegister.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()
            val username = binding.edUsername.text.toString()
            val phoneNumber = binding.edPhone.text.toString()
            val fields = listOf(email, password, username, phoneNumber)

            if (fields.all { it.isNotEmpty() }) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val userId = firebaseAuth.currentUser!!.uid
                            val user = User(username, email, phoneNumber)
                            database.child("users").child(userId).setValue(user)
                            Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()
                            toLogin()
                        } else {
                            val parts = task.exception.toString().split(":")
                            val errorMessage = parts[1].trim()
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}