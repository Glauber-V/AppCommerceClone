package com.example.appcommerceclone.ui

import androidx.fragment.app.Fragment
import com.example.appcommerceclone.model.user.User
import com.example.appcommerceclone.ui.BaseNavigation.navigateToConnectionFragment
import com.example.appcommerceclone.ui.BaseNavigation.navigateToLoginFragment
import com.example.appcommerceclone.viewmodels.ConnectivityViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel

object BaseUser {

    inline fun Fragment.verifyUserConnectionToProceed(
        connectivityViewModel: ConnectivityViewModel,
        crossinline function: () -> Unit = {}
    ) {
        connectivityViewModel.isConnected.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) function()
            else navigateToConnectionFragment()
        }
    }

    inline fun Fragment.verifyUserExistsToProceed(
        userViewModel: UserViewModel,
        crossinline function: (user: User) -> Unit = {}
    ) {
        userViewModel.loggedUser.observe(viewLifecycleOwner) { user ->
            if (user != null) function(user)
            else navigateToLoginFragment()
        }
    }
}