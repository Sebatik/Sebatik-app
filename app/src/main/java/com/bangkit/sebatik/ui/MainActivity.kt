package com.bangkit.sebatik.ui

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bangkit.sebatik.R
import com.bangkit.sebatik.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import me.ibrahimsn.lib.SmoothBottomBar

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.setGraph(R.navigation.mobile_navigation)

        val navView: BottomNavigationView = binding.bottomNavbar
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.teamFragment -> navView.visibility = View.GONE
                R.id.addProductFragment -> navView.visibility = View.GONE
                R.id.detailProductFragment -> navView.visibility = View.GONE
                else -> navView.visibility = View.VISIBLE
            }
        }
    }
}