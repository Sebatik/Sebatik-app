package com.bangkit.sebatik.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.sebatik.R
import com.bangkit.sebatik.data.models.User
import com.bangkit.sebatik.databinding.ActivityRegisterBinding
import com.bangkit.sebatik.ui.login.LoginActivity
import com.bangkit.sebatik.util.LoadingDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference
        firebaseAuth = Firebase.auth
        loadingDialog = LoadingDialog(this)

        binding.apply {
            tvToSignin.setOnClickListener { toLogin() }
            btnRegister.setOnClickListener { createAccount() }
        }
    }

    private fun createAccount() {
        loadingDialog.showLoading()
        val email = binding.edEmail.text.toString()
        val password = binding.edPassword.text.toString()
        val username = binding.edUsername.text.toString()
        val phoneNumber = binding.edPhone.text.toString()
        val fields = listOf(email, password, username, phoneNumber)

        if (fields.all { it.isNotEmpty() }) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        loadingDialog.hideLoading()
                        val userId = firebaseAuth.currentUser!!.uid
                        val user = User(username, email, phoneNumber)
                        database.child("users").child(userId).setValue(user)
                        Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                        toLogin()
                    } else {
                        loadingDialog.hideLoading()
                        val parts = task.exception.toString().split(":")
                        val errorMessage = parts[1].trim()
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            loadingDialog.hideLoading()
            Toast.makeText(this, getString(R.string.empty_field), Toast.LENGTH_SHORT).show()
        }
    }

    private fun toLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
}