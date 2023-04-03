package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.product.FakeProductsProvider.Companion.productJewelery
import com.example.appcommerceclone.data.user.FakeUserProvider.Companion.firstUser
import com.example.appcommerceclone.data.user.UserAuthenticator
import com.example.appcommerceclone.data.user.UserOrders
import com.example.appcommerceclone.data.user.UserPreferences
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.UsersModule
import com.example.appcommerceclone.model.order.Order
import com.example.appcommerceclone.model.order.OrderedProduct
import com.example.appcommerceclone.ui.order.OrdersFragment
import com.example.appcommerceclone.util.*
import com.example.appcommerceclone.viewmodels.UserOrdersViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@UninstallModules(UsersModule::class, DispatcherModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class OrdersFragmentLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testNavHostControllerRule = TestNavHostControllerRule(R.id.orders_fragment)

    @get:Rule(order = 2)
    val testMainDispatcherRule = TestMainDispatcherRule()

    @Inject lateinit var userAuthenticator: UserAuthenticator
    @Inject lateinit var userPreferences: UserPreferences
    @Inject lateinit var userOrders: UserOrders
    @Inject lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var navHostController: TestNavHostController
    private lateinit var userViewModel: UserViewModel
    private lateinit var userOrdersViewModel: UserOrdersViewModel
    private lateinit var factory: TestFragmentFactory

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
        userViewModel = UserViewModel(userAuthenticator, userPreferences, dispatcherProvider)
        userOrdersViewModel = UserOrdersViewModel(userOrders, dispatcherProvider)
        factory = TestFragmentFactory(userViewModelTest = userViewModel, userOrdersViewModelTest = userOrdersViewModel)
    }

    @Test
    fun launchOrdersFragments_noUser_navigateToLoginFragment() = runTest {

        val user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNull()

        launchFragmentInHiltContainer<OrdersFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.orders_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)
        }
    }

    @Test
    fun launchOrdersFragment_withUser_stayInOrdersFragment() = runTest {

        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        val user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNotNull()
        assertThat(user).isEqualTo(firstUser)

        launchFragmentInHiltContainer<OrdersFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.user_login_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.orders_fragment)
        }
    }

    @Test
    fun launchOrdersFragment_withUser_verifyOrdersListHasSize2() = runTest {

        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        val user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNotNull()
        assertThat(user).isEqualTo(firstUser)

        var orders = userOrdersViewModel.orders.getOrAwaitValue()
        assertThat(orders).isEmpty()

        launchFragmentInHiltContainer<OrdersFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.user_login_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.orders_fragment)

            advanceUntilIdle()

            orders = userOrdersViewModel.orders.getOrAwaitValue()
            assertThat(orders).isNotEmpty()
            assertThat(orders).hasSize(2)
        }
    }

    @Test
    fun launchOrdersFragment_withUser_processUnfinishedOrder() = runTest {

        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        val user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNotNull()
        assertThat(user).isEqualTo(firstUser)

        var orders = userOrdersViewModel.orders.getOrAwaitValue()
        assertThat(orders).isEmpty()

        val orderedProduct = OrderedProduct(productJewelery)
        val orderedProducts = mutableListOf(orderedProduct)
        val total = orderedProducts.sumOf { it.product.price * it.quantity }
        var order = Order(orderedProducts = orderedProducts, total = total)
        userOrdersViewModel.receiveOrder(order)

        launchFragmentInHiltContainer<OrdersFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.user_login_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.orders_fragment)

            advanceUntilIdle()

            orders = userOrdersViewModel.orders.getOrAwaitValue()
            assertThat(orders).isNotEmpty()
            assertThat(orders).hasSize(1)

            order = orders.first()
            assertThat(order.id).isNotEqualTo(0)
            assertThat(order.userId).isEqualTo(user!!.id)
            assertThat(order.date).isNotEqualTo("")
            assertThat(order.orderedProducts).isEqualTo(orderedProducts)
            assertThat(order.total).isEqualTo(total)
        }
    }
}