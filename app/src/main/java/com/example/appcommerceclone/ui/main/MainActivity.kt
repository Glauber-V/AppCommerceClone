package com.example.appcommerceclone.ui.main

import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.appcommerceclone.NavigationGraphDirections
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.user.model.User
import com.example.appcommerceclone.databinding.ActivityMainBinding
import com.example.appcommerceclone.ui.connection.ConnectivityViewModel
import com.example.appcommerceclone.ui.product.ProductViewModel
import com.example.appcommerceclone.ui.user.UserViewModel
import com.example.appcommerceclone.util.navigateToProductsFragment
import com.example.appcommerceclone.util.navigateWithCredentials
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener, OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var navHostFragment: MainNavHostFragment
    private lateinit var navController: NavController

    private val connectivityViewModel: ConnectivityViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private var currentDestination: NavDestination? = null
    private var currentUser: User? = null

    private val onBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT)) closeDrawerIfOpen()
            else finish()
        }
    }

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

        navController.addOnDestinationChangedListener(this@MainActivity)
        navigationView.setNavigationItemSelectedListener(this@MainActivity)
        onBackPressedDispatcher.addCallback(this@MainActivity, onBackPressedCallback)

        connectivityViewModel.isConnected.observe(this@MainActivity) { hasConnection ->
            binding.actionBarMessage.visibility = if (!hasConnection) View.VISIBLE else View.GONE
            if (hasConnection) productViewModel.updateProductListOnConnectionReestablish()
        }

        userViewModel.currentUser.observe(this@MainActivity) { _currentUser ->
            currentUser = _currentUser
            if (currentUser != null) {
                navigationView.menu.findItem(R.id.menu_login).isVisible = false
                navigationView.menu.findItem(R.id.menu_profile).isVisible = true
                navigationView.menu.findItem(R.id.menu_logout).isVisible = true
            } else {
                navigationView.menu.findItem(R.id.menu_login).isVisible = true
                navigationView.menu.findItem(R.id.menu_profile).isVisible = false
                navigationView.menu.findItem(R.id.menu_logout).isVisible = false
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_login -> {
                navController.navigate(NavigationGraphDirections.actionGlobalLoginFragment())
                true
            }

            R.id.menu_profile -> {
                navController.navigateWithCredentials(currentUser, NavigationGraphDirections.actionGlobalProfileFragment())
                true
            }

            R.id.menu_favorites -> {
                navController.navigateWithCredentials(currentUser, NavigationGraphDirections.actionGlobalFavoritesFragment())
                true
            }

            R.id.menu_categories -> {
                navController.navigate(NavigationGraphDirections.actionGlobalCategoriesFragment())
                true
            }

            R.id.menu_orders -> {
                navController.navigateWithCredentials(currentUser, NavigationGraphDirections.actionGlobalOrdersFragment())
                true
            }

            R.id.menu_cart -> {
                navController.navigate(NavigationGraphDirections.actionGlobalCartFragment())
                true
            }

            R.id.menu_logout -> {
                userViewModel.logout()
                true
            }

            else -> {
                false
            }
        }
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        currentDestination = destination

        if (currentDestination?.id == R.id.products_fragment) {
            unlockDrawer()
            onBackPressedCallback.isEnabled = true
        } else {
            closeDrawerIfOpen()
            lockDrawer()
            onBackPressedCallback.isEnabled = false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return when (currentDestination?.id) {
            R.id.login_fragment -> navController.navigateToProductsFragment()
            R.id.favorites_fragment -> navController.navigateToProductsFragment()
            R.id.cart_fragment -> navController.navigateToProductsFragment()
            R.id.orders_fragment -> navController.navigateToProductsFragment()
            else -> {
                navController.navigateUp(drawerLayout)
            }
        }
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