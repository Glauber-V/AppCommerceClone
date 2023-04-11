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
import com.example.appcommerceclone.NavigationGraphDirections
import com.example.appcommerceclone.R
import com.example.appcommerceclone.databinding.ActivityMainBinding
import com.example.appcommerceclone.util.ViewExt.isLocked
import com.example.appcommerceclone.util.ViewExt.isNavigationUpEnabled
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

        val onBackPressedCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) drawerLayout.closeDrawer(Gravity.LEFT)
                else finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        setupActionBarWithNavController(navController, drawerLayout)
        navigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.products_fragment -> {
                    binding.drawerLayout.isLocked(false)
                    onBackPressedCallback.isEnabled = true
                }
                R.id.user_login_fragment -> {
                    supportActionBar?.isNavigationUpEnabled(false)
                    binding.drawerLayout.isLocked(true)
                    onBackPressedCallback.isEnabled = false
                }
                R.id.connection_fragment -> {
                    supportActionBar?.isNavigationUpEnabled(false)
                    binding.drawerLayout.isLocked(true)
                    onBackPressedCallback.isEnabled = false
                }
                else -> {
                    supportActionBar?.isNavigationUpEnabled(true)
                    binding.drawerLayout.isLocked(true)
                    onBackPressedCallback.isEnabled = false
                }
            }
        }

        connectivityViewModel.isConnected.observe(this) { hasConnection ->
            if (!hasConnection) navController.navigate(NavigationGraphDirections.actionGlobalConnectionFragment())
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(drawerLayout)
    }
}