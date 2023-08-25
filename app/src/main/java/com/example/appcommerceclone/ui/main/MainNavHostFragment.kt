package com.example.appcommerceclone.ui.main

import android.content.Context
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainNavHostFragment : NavHostFragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val fragmentFactory = DefaultFragmentFactory(requireActivity())
        childFragmentManager.fragmentFactory = fragmentFactory
    }
}