package com.bangkit.sebatik.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bangkit.sebatik.R
import com.bangkit.sebatik.data.UserPreferences
import com.bangkit.sebatik.data.dataStore
import com.bangkit.sebatik.databinding.ActivitySplashBinding
import com.bangkit.sebatik.ui.MainActivity
import com.bangkit.sebatik.ui.login.LoginActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val delayTime = 1500L

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        Handler(Looper.getMainLooper()).postDelayed({
            val userLogin = runBlocking {
                UserPreferences.getInstance(this@SplashActivity.dataStore).getLogin().first()
            }
            if (userLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            }
        }, delayTime)
    }
}