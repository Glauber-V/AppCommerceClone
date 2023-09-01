package com.example.appcommerceclone.util

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.NavigationGraphDirections
import com.example.appcommerceclone.data.user.model.User

fun NavController.navigateWithCredentials(user: User?, directions: NavDirections) {
    if (user != null) navigate(directions)
    else navigate(NavigationGraphDirections.actionGlobalLoginFragment())
}

fun NavController.navigateToProductsFragment(): Boolean {
    val startDestination = graph.startDestinationId
    val navOptions = NavOptions.Builder().setPopUpTo(startDestination, true).build()
    navigate(startDestination, null, navOptions)
    return true
}

fun Fragment.onBackPressedReturnToProductsFragment(enable: Boolean = true) {
    requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(enable) {
        override fun handleOnBackPressed() {
            findNavController().navigateToProductsFragment()
        }
    })
}