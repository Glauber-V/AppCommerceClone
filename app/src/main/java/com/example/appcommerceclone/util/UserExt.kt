package com.example.appcommerceclone.util

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.NavigationGraphDirections
import com.example.appcommerceclone.model.user.User
import com.example.appcommerceclone.viewmodels.UserViewModel

object UserExt {

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