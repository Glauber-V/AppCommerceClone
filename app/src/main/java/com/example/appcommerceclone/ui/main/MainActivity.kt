package com.example.appcommerceclone.ui.main

import android.os.Bundle
import android.view.Gravity
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.appcommerceclone.R
import com.example.appcommerceclone.databinding.ActivityMainBinding
import com.example.appcommerceclone.ui.connection.ConnectivityViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: MainNavHostFragment
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    private val connectivityViewModel: ConnectivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.activityMainTopBar.actionBar)

        navHostFragment = binding.navHostFragment.getFragment()
        navController = navHostFragment.navController

        drawerLayout = binding.drawerLayout
        navigationView = binding.navView

        val onBackPressedCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                closeDrawerIfOpen()
            }
        }
        onBackPressedDispatcher.addCallback(this@MainActivity, onBackPressedCallback)

        setupActionBarWithNavController(navController, drawerLayout)
        navigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.products_fragment -> {
                    unlockDrawer()
                    onBackPressedCallback.isEnabled = true
                }

                else -> {
                    lockDrawer()
                    onBackPressedCallback.isEnabled = false
                    closeDrawerIfOpen()
                }
            }
        }

        connectivityViewModel.isConnected.observe(this) { hasConnection ->
            binding.activityMainTopBar.hasConnection = hasConnection
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(drawerLayout)
    }

    private fun closeDrawerIfOpen() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) drawerLayout.closeDrawer(Gravity.LEFT)
    }

    private fun lockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun unlockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }
}