package com.example.appcommerceclone.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appcommerceclone.AppCommerceNavHost
import com.example.appcommerceclone.ui.connection.ConnectivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val connectivityViewModel by viewModels<ConnectivityViewModel>()
                val isConnected by connectivityViewModel.isConnected.observeAsState(initial = false)
                AppCommerceNavHost(
                    isConnected = isConnected,
                    productViewModel = hiltViewModel(),
                    favoritesViewModel = hiltViewModel(),
                    cartViewModel = hiltViewModel(),
                    userViewModel = hiltViewModel(),
                    userOrdersViewModel = hiltViewModel()
                )
            }
        }
    }
}