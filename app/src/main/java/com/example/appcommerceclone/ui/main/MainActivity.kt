package com.example.appcommerceclone.ui.main

import android.os.Bundle
import android.view.Gravity
import android.view.View
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
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var navHostFragment: MainNavHostFragment
    private lateinit var navController: NavController

    private val connectivityViewModel: ConnectivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        drawerLayout = binding.drawerLayout
        navigationView = binding.navView
        navHostFragment = binding.navHostFragment.getFragment()
        navController = navHostFragment.navController

        setContentView(binding.root)
        setSupportActionBar(binding.actionBar)
        setupActionBarWithNavController(navController, drawerLayout)
        navigationView.setupWithNavController(navController)

        val onBackPressedCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                closeDrawerIfOpen()
            }
        }
        onBackPressedDispatcher.addCallback(this@MainActivity, onBackPressedCallback)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.products_fragment) {
                unlockDrawer()
                onBackPressedCallback.isEnabled = true
            } else {
                closeDrawerIfOpen()
                lockDrawer()
                onBackPressedCallback.isEnabled = false
            }
        }

        connectivityViewModel.isConnected.observe(this) { hasConnection ->
            binding.actionBarMessage.visibility = if (!hasConnection) View.VISIBLE else View.GONE
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