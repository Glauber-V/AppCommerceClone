package com.example.appcommerceclone.util

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.R

fun Fragment.navigateToProductsFragment(): Boolean {
    return findNavController().popBackStack(destinationId = R.id.products_fragment, inclusive = true)
}

fun Fragment.onBackPressedReturnToProductsFragment(enable: Boolean = true) {
    requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(enable) {
        override fun handleOnBackPressed() {
            val navController = findNavController()
            val startDestination = navController.graph.startDestinationId
            val navOptions = NavOptions.Builder().setPopUpTo(startDestination, true).build()
            navController.navigate(startDestination, null, navOptions)
        }
    })
}