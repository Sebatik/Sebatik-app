package com.bangkit.sebatik.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.sebatik.R
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.data.dataStore
import com.bangkit.sebatik.databinding.ActivityLoginBinding
import com.bangkit.sebatik.ui.MainActivity
import com.bangkit.sebatik.ui.register.RegisterActivity
import com.bangkit.sebatik.util.LoadingDialog
import com.bangkit.sebatik.util.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var loadingDialog: LoadingDialog
    private val loginViewModel by viewModels<LoginViewModel>() {
        ViewModelFactory.getInstance(application, UserPreferences.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        loadingDialog = LoadingDialog(this)

        binding.apply {
            tvToSignup.setOnClickListener { toSignup() }
            btnLogin.setOnClickListener { login() }
        }
    }

    private fun login() {
        loadingDialog.showLoading()
        val email = binding.edEmail.text.toString()
        val password = binding.edPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        loadingDialog.hideLoading()
                        firebaseAuth.currentUser!!.getIdToken(true)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val idToken = it.result?.token
                                    loginViewModel.isLogin(idToken.toString())
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                                    finish()
                                }
                            }
                    } else {
                        loadingDialog.hideLoading()
                        Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            loadingDialog.hideLoading()
            Toast.makeText(this, getString(R.string.empty_field), Toast.LENGTH_SHORT).show()
        }
    }

    private fun toSignup() {
        startActivity(Intent(this, RegisterActivity::class.java))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}