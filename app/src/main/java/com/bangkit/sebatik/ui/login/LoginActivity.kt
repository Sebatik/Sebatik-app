package com.bangkit.sebatik.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.sebatik.R
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.data.dataStore
import com.bangkit.sebatik.databinding.ActivityLoginBinding
import com.bangkit.sebatik.ui.MainActivity
import com.bangkit.sebatik.ui.register.RegisterActivity
import com.bangkit.sebatik.util.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val loginViewModel by viewModels<LoginViewModel>() {
        ViewModelFactory.getInstance(application, UserPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.tvToSignup.setOnClickListener{ toSignup() }
        binding.btnLogin.setOnClickListener {
            showLoading(true)
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            showLoading(false)
                            firebaseAuth.currentUser!!.getIdToken(true)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        val idToken = it.result?.token
                                        loginViewModel.isLogin(idToken.toString())
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                        } else {
                            showLoading(false)
                            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Empty Fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun toSignup() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}