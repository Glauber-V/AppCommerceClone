package com.example.appcommerceclone.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.example.appcommerceclone.ui.cart.CartFragment
import com.example.appcommerceclone.ui.cart.CartViewModel
import com.example.appcommerceclone.ui.favorites.FavoritesFragment
import com.example.appcommerceclone.ui.favorites.FavoritesViewModel
import com.example.appcommerceclone.ui.order.OrdersFragment
import com.example.appcommerceclone.ui.order.UserOrdersViewModel
import com.example.appcommerceclone.ui.product.CategoriesFragment
import com.example.appcommerceclone.ui.product.ProductDetailFragment
import com.example.appcommerceclone.ui.product.ProductViewModel
import com.example.appcommerceclone.ui.product.ProductsFragment
import com.example.appcommerceclone.ui.user.LoginFragment
import com.example.appcommerceclone.ui.user.PictureChooserDialogFragment
import com.example.appcommerceclone.ui.user.ProfileFragment
import com.example.appcommerceclone.ui.user.RegisterFragment
import com.example.appcommerceclone.ui.user.UserViewModel

class DefaultFragmentFactory(container: FragmentActivity) : FragmentFactory() {

    private val productViewModel = ViewModelProvider(container)[ProductViewModel::class.java]
    private val favoritesViewModel = ViewModelProvider(container)[FavoritesViewModel::class.java]
    private val cartViewModel = ViewModelProvider(container)[CartViewModel::class.java]
    private val userViewModel = ViewModelProvider(container)[UserViewModel::class.java]
    private val userOrdersViewModel = ViewModelProvider(container)[UserOrdersViewModel::class.java]

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            ProductsFragment::class.java.name -> ProductsFragment(productViewModel)
            ProductDetailFragment::class.java.name ->
                ProductDetailFragment(productViewModel, userViewModel, favoritesViewModel, userOrdersViewModel, cartViewModel)

            CategoriesFragment::class.java.name -> CategoriesFragment(productViewModel)
            FavoritesFragment::class.java.name -> FavoritesFragment(favoritesViewModel)
            CartFragment::class.java.name -> CartFragment(cartViewModel, userViewModel, userOrdersViewModel)
            OrdersFragment::class.java.name -> OrdersFragment(userOrdersViewModel)
            LoginFragment::class.java.name -> LoginFragment(userViewModel)
            RegisterFragment::class.java.name -> RegisterFragment()
            ProfileFragment::class.java.name -> ProfileFragment(userViewModel)
            PictureChooserDialogFragment::class.java.name -> PictureChooserDialogFragment(userViewModel)

            else -> super.instantiate(classLoader, className)
        }
    }
}