package com.example.appcommerceclone.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.appcommerceclone.ui.cart.CartFragment
import com.example.appcommerceclone.ui.connection.ConnectionFragment
import com.example.appcommerceclone.ui.favorites.FavoritesFragment
import com.example.appcommerceclone.ui.order.OrdersFragment
import com.example.appcommerceclone.ui.product.CategoriesFragment
import com.example.appcommerceclone.ui.product.ProductDetailFragment
import com.example.appcommerceclone.ui.product.ProductsFragment
import com.example.appcommerceclone.ui.user.PictureChooserDialogFragment
import com.example.appcommerceclone.ui.user.UserLoginFragment
import com.example.appcommerceclone.ui.user.UserProfileFragment
import com.example.appcommerceclone.ui.user.UserRegisterFragment
import com.example.appcommerceclone.viewmodels.*

class TestFragmentFactory(
    private val connectivityViewModel: ConnectivityViewModel? = null,
    private val productViewModel: ProductViewModel? = null,
    private val favoritesViewModel: FavoritesViewModel? = null,
    private val cartViewModel: CartViewModel? = null,
    private val userViewModel: UserViewModel? = null,
    private val userOrdersViewModel: UserOrdersViewModel? = null
) : FragmentFactory() {


    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            ConnectionFragment::class.java.name -> {
                ConnectionFragment(connectivityViewModel!!)
            }
            ProductsFragment::class.java.name -> {
                ProductsFragment(productViewModel!!)
            }
            ProductDetailFragment::class.java.name -> {
                ProductDetailFragment(productViewModel!!, favoritesViewModel!!, cartViewModel!!)
            }
            CategoriesFragment::class.java.name -> {
                CategoriesFragment(productViewModel!!)
            }
            FavoritesFragment::class.java.name -> {
                FavoritesFragment(userViewModel!!, favoritesViewModel!!)
            }
            CartFragment::class.java.name -> {
                CartFragment(cartViewModel!!, userOrdersViewModel!!)
            }
            OrdersFragment::class.java.name -> {
                OrdersFragment(userViewModel!!, userOrdersViewModel!!)
            }
            UserLoginFragment::class.java.name -> {
                UserLoginFragment(userViewModel!!)
            }
            UserRegisterFragment::class.java.name -> {
                UserRegisterFragment()
            }
            UserProfileFragment::class.java.name -> {
                UserProfileFragment(userViewModel!!)
            }
            PictureChooserDialogFragment::class.java.name -> {
                PictureChooserDialogFragment(userViewModel!!)
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}