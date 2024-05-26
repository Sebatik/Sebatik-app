package com.bangkit.sebatik.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.bangkit.sebatik.R
import com.bangkit.sebatik.databinding.ActivityMainBinding
import com.bangkit.sebatik.ui.favorites.FavoriteFragment
import com.bangkit.sebatik.ui.history.HistoryFragment
import com.bangkit.sebatik.ui.home.HomeFragment
import com.bangkit.sebatik.ui.scan.ScanFragment
import com.bangkit.sebatik.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavbar.setOnItemSelectedListener{
            when(it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.favorites -> replaceFragment(FavoriteFragment())
                R.id.scan -> replaceFragment(ScanFragment())
                R.id.history -> replaceFragment(HistoryFragment())
                R.id.setting -> replaceFragment(SettingsFragment())
                else -> {

                }
            }
            true
        }


    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}