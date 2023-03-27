package com.example.appcommerceclone.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.NavigationGraphDirections
import com.example.appcommerceclone.model.user.User
import com.example.appcommerceclone.viewmodels.ConnectivityViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel

object UserExt {

    fun AppCompatActivity.observeUserConnectionStatus(navController: NavController, connection: ConnectivityViewModel) {
        val toDestination = NavigationGraphDirections.actionGlobalConnectionFragment()
        connection.isConnected.observe(this) { isConnected ->
            if (!isConnected) navController.navigate(toDestination)
        }
    }

    inline fun Fragment.verifyUserExistsToProceed(
        userViewModel: UserViewModel,
        crossinline function: (user: User) -> Unit = {}
    ) {
        userViewModel.loggedUser.observe(viewLifecycleOwner) { user ->
            if (user != null) function(user)
            else findNavController().navigate(NavigationGraphDirections.actionGlobalUserLoginFragment())
        }
    }
}