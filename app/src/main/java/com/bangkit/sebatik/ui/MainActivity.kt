package com.bangkit.sebatik.ui

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bangkit.sebatik.R
import com.bangkit.sebatik.databinding.ActivityMainBinding
import me.ibrahimsn.lib.SmoothBottomBar

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.setGraph(R.navigation.mobile_navigation)

        val navView: SmoothBottomBar = binding.bottomNavbar
        setupNavbar()

        navController.addOnDestinationChangedListener {_, destination, _ ->
            when (destination.id) {
                R.id.teamFragment -> navView.visibility = View.GONE
                R.id.addProductFragment -> navView.visibility = View.GONE
                R.id.settingsFragment -> navView.visibility = View.GONE
                else -> navView.visibility = View.VISIBLE
            }
        }
    }

    private fun setupNavbar() {
        val anchorView: View = binding.bottomNavbar
        val popupMenu = PopupMenu(this, anchorView)
        popupMenu.inflate(R.menu.nav_menu)
        val menu = popupMenu.menu
        binding.bottomNavbar.setupWithNavController(menu, navController)
    }
}