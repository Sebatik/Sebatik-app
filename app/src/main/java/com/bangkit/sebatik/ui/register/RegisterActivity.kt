package com.bangkit.sebatik.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns.EMAIL_ADDRESS
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.sebatik.R
import com.bangkit.sebatik.data.models.User
import com.bangkit.sebatik.databinding.ActivityLoginBinding
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

            if (!EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edEmail.error = "Invalid Email"
            }
            if (TextUtils.isEmpty(password) || password.length < 6 ) {
                binding.edUsername.error = "Invalid Password"
            }
            if (TextUtils.isEmpty(phoneNumber)) {
                binding.edPhone.error = "Enter Phone Number"
            }
            if (TextUtils.isEmpty(username)) {
                binding.edUsername.error = "Enter Username"
            }

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = firebaseAuth.currentUser!!.uid
                        val user = User(username, email, phoneNumber)
                        database.child("users").child(userId).setValue(user)
                        Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun toLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}