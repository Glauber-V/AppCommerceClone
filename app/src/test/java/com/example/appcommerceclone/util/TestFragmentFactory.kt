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
    private val connectivityViewModelTest: ConnectivityViewModel? = null,
    private val productViewModelTest: ProductViewModel? = null,
    private val favoritesViewModelTest: FavoritesViewModel? = null,
    private val cartViewModelTest: CartViewModel? = null,
    private val userViewModelTest: UserViewModel? = null,
    private val userOrdersViewModelTest: UserOrdersViewModel? = null
) : FragmentFactory() {


    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            ConnectionFragment::class.java.name -> {
                ConnectionFragment(connectivityViewModelTest!!)
            }
            ProductsFragment::class.java.name -> {
                ProductsFragment(productViewModelTest!!)
            }
            ProductDetailFragment::class.java.name -> {
                ProductDetailFragment(productViewModelTest!!, favoritesViewModelTest!!, cartViewModelTest!!)
            }
            CategoriesFragment::class.java.name -> {
                CategoriesFragment(productViewModelTest!!)
            }
            FavoritesFragment::class.java.name -> {
                FavoritesFragment(userViewModelTest!!, favoritesViewModelTest!!)
            }
            CartFragment::class.java.name -> {
                CartFragment(cartViewModelTest!!, userOrdersViewModelTest!!)
            }
            OrdersFragment::class.java.name -> {
                OrdersFragment(userViewModelTest!!, userOrdersViewModelTest!!)
            }
            UserLoginFragment::class.java.name -> {
                UserLoginFragment(userViewModelTest!!)
            }
            UserRegisterFragment::class.java.name -> {
                UserRegisterFragment()
            }
            UserProfileFragment::class.java.name -> {
                UserProfileFragment(userViewModelTest!!)
            }
            PictureChooserDialogFragment::class.java.name -> {
                PictureChooserDialogFragment(userViewModelTest!!)
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}