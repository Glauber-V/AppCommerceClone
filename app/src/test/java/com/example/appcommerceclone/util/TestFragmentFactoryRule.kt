package com.example.appcommerceclone.util

import com.example.appcommerceclone.data.connection.FakeConnectivityObserver
import com.example.appcommerceclone.data.product.FakeProductsRepository
import com.example.appcommerceclone.data.user.FakeUserAuthenticator
import com.example.appcommerceclone.data.user.FakeUserOrders
import com.example.appcommerceclone.data.user.FakeUserPreferences
import com.example.appcommerceclone.viewmodels.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class TestFragmentFactoryRule : TestWatcher() {

    var connectivityViewModel: ConnectivityViewModel? = null
    var productViewModel: ProductViewModel? = null
    var favoritesViewModel: FavoritesViewModel? = null
    var cartViewModel: CartViewModel? = null
    var userViewModel: UserViewModel? = null
    var userOrdersViewModel: UserOrdersViewModel? = null

    var factory: TestFragmentFactory? = null

    override fun starting(description: Description) {
        super.starting(description)

        connectivityViewModel = ConnectivityViewModel(FakeConnectivityObserver())
        productViewModel = ProductViewModel(FakeProductsRepository())
        favoritesViewModel = FavoritesViewModel()
        cartViewModel = CartViewModel()
        userViewModel = UserViewModel(FakeUserAuthenticator(), FakeUserPreferences())
        userOrdersViewModel = UserOrdersViewModel(FakeUserOrders())

        factory = TestFragmentFactory(
            connectivityViewModel,
            productViewModel,
            favoritesViewModel,
            cartViewModel,
            userViewModel,
            userOrdersViewModel
        )
    }

    override fun finished(description: Description) {
        super.finished(description)

        connectivityViewModel = null
        productViewModel = null
        favoritesViewModel = null
        cartViewModel = null
        userViewModel = null
        userOrdersViewModel = null

        factory = null
    }
}