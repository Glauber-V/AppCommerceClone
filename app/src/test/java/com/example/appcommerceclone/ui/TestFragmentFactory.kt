package com.example.appcommerceclone.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
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

class TestFragmentFactory(
    private val productViewModelTest: ProductViewModel? = null,
    private val favoritesViewModelTest: FavoritesViewModel? = null,
    private val cartViewModelTest: CartViewModel? = null,
    private val userViewModelTest: UserViewModel? = null,
    private val userOrdersViewModelTest: UserOrdersViewModel? = null
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            ProductsFragment::class.java.name -> ProductsFragment(productViewModelTest!!)
            ProductDetailFragment::class.java.name -> ProductDetailFragment(
                productViewModelTest!!,
                userViewModelTest!!,
                favoritesViewModelTest!!,
                userOrdersViewModelTest!!,
                cartViewModelTest!!
            )

            CategoriesFragment::class.java.name -> CategoriesFragment(productViewModelTest!!)
            FavoritesFragment::class.java.name -> FavoritesFragment(favoritesViewModelTest!!)
            CartFragment::class.java.name -> CartFragment(
                cartViewModelTest!!,
                userViewModelTest!!,
                userOrdersViewModelTest!!
            )

            OrdersFragment::class.java.name -> OrdersFragment(userOrdersViewModelTest!!)
            LoginFragment::class.java.name -> LoginFragment(userViewModelTest!!)
            RegisterFragment::class.java.name -> RegisterFragment()
            ProfileFragment::class.java.name -> ProfileFragment(userViewModelTest!!)
            PictureChooserDialogFragment::class.java.name -> PictureChooserDialogFragment(userViewModelTest!!)

            else -> super.instantiate(classLoader, className)
        }
    }
}