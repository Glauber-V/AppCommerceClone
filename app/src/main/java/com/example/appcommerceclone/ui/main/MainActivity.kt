package com.example.appcommerceclone.ui.main

import android.os.Bundle
import android.view.Gravity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.appcommerceclone.R
import com.example.appcommerceclone.databinding.ActivityMainBinding
import com.example.appcommerceclone.util.UserExt.observeUserConnectionStatus
import com.example.appcommerceclone.viewmodels.ConnectivityViewModel
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

        navHostFragment = binding.navHostFragment.getFragment()
        navController = navHostFragment.navController

        drawerLayout = binding.drawerLayout
        navigationView = binding.navView

        setupActionBarWithNavController(navController, drawerLayout)
        navigationView.setupWithNavController(navController)

        observeUserConnectionStatus(navController, connectivityViewModel)
        setupOnDestinationChangedListener()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(drawerLayout)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.connection_fragment) finish().also { return }
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) drawerLayout.closeDrawer(Gravity.LEFT)
        else super.onBackPressed()
    }


    private fun setupOnDestinationChangedListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.connection_fragment) {
                supportActionBar?.apply {
                    setHomeButtonEnabled(false)
                    setDisplayHomeAsUpEnabled(false)
                    setDisplayShowHomeEnabled(false)
                }
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                supportActionBar?.apply {
                    setHomeButtonEnabled(true)
                    setDisplayHomeAsUpEnabled(true)
                    setDisplayShowHomeEnabled(true)
                }
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        }
    }
}