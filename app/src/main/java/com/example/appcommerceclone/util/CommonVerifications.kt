package com.example.appcommerceclone.util

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.NavigationGraphDirections
import com.example.appcommerceclone.model.user.User
import com.example.appcommerceclone.viewmodels.ConnectivityViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel

object CommonVerifications {

    inline fun Fragment.verifyUserConnectionToProceed(
        connectivityViewModel: ConnectivityViewModel,
        crossinline function: () -> Unit = {}
    ) {
        connectivityViewModel.isConnected.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) function()
            else findNavController().navigate(NavigationGraphDirections.actionGlobalConnectionFragment())
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