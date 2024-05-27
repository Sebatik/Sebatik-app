package com.bangkit.sebatik.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.sebatik.R
import com.bangkit.sebatik.databinding.ActivityLoginBinding
import com.bangkit.sebatik.databinding.ActivityRegisterBinding
import com.bangkit.sebatik.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvToSignin.setOnClickListener {toLogin()}
        binding.btnRegister.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()
                            toLogin()
                            finish()
                        } else {
                            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Empty Fields Are Not Allowed", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun toLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

}