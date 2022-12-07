package com.example.appcommerceclone.ui

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.NavigationGraphDirections
import com.example.appcommerceclone.model.product.Product
import com.example.appcommerceclone.util.Constants

object BaseNavigation {

    fun Fragment.navigateToConnectionFragment() {
        val action = NavigationGraphDirections.actionGlobalConnectionFragment()
        findNavController().navigate(action)
    }

    fun Fragment.navigateToLoginFragment() {
        val action = NavigationGraphDirections.actionGlobalUserLoginFragment()
        findNavController().navigate(action)
    }

    fun UserLoginFragment.navigateToRegisterFragment() {
        val action = UserLoginFragmentDirections.actionUserLoginFragmentToUserRegisterFragment()
        findNavController().navigate(action)
    }

    fun ProductsFragment.navigateToProductDetail(product: Product) {
        val action =
            if (product.category == Constants.CATEGORY_ELECTRONICS || product.category == Constants.CATEGORY_JEWELRY) {
                ProductsFragmentDirections.actionProductsFragmentToProductLessDetailFragment()
            } else {
                ProductsFragmentDirections.actionProductsFragmentToProductFullDetailFragment()
            }
        findNavController().navigate(action)
    }

    fun ProductLessDetailFragment.navigateToFavoritesFragment() {
        val action = ProductLessDetailFragmentDirections.actionProductLessDetailFragmentToFavoritesFragment()
        findNavController().navigate(action)
    }

    fun ProductFullDetailFragment.navigateToFavoritesFragment() {
        val action = ProductFullDetailFragmentDirections.actionProductFullDetailFragmentToFavoritesFragment()
        findNavController().navigate(action)
    }

    fun ProductLessDetailFragment.navigateToCartFragment() {
        val action = ProductLessDetailFragmentDirections.actionProductLessDetailFragmentToCartFragment()
        findNavController().navigate(action)
    }

    fun ProductFullDetailFragment.navigateToCartFragment() {
        val action = ProductFullDetailFragmentDirections.actionProductFullDetailFragmentToCartFragment()
        findNavController().navigate(action)
    }

    fun CartFragment.navigateToOrdersFragment() {
        val action = CartFragmentDirections.actionCartFragmentToOrdersFragment()
        findNavController().navigate(action)
    }
}