package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.UsersModule
import com.example.appcommerceclone.ui.order.OrdersFragment
import com.example.appcommerceclone.ui.order.UserOrdersViewModel
import com.example.appcommerceclone.util.TestNavHostControllerRule
import com.example.appcommerceclone.util.assertThatCurrentDestinationIsEqualTo
import com.example.appcommerceclone.util.assertThatCurrentDestinationIsNotEqualTo
import com.example.appcommerceclone.util.assertThatOrdersPlaceholderIsInSyncWithListState
import com.example.appcommerceclone.util.atPosition
import com.example.appcommerceclone.util.firstUser
import com.example.appcommerceclone.util.formatTotalPrice
import com.example.appcommerceclone.util.getOrAwaitValue
import com.example.appcommerceclone.util.launchFragmentInHiltContainer
import com.example.appcommerceclone.util.orderedProductList
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@UninstallModules(UsersModule::class, DispatcherModule::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class OrdersFragmentLocalTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val testNavHostControllerRule = TestNavHostControllerRule(R.id.orders_fragment)

    private lateinit var navHostController: TestNavHostController
    private lateinit var userOrdersViewModel: UserOrdersViewModel
    private lateinit var factory: TestFragmentFactory

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
        userOrdersViewModel = UserOrdersViewModel()
        factory = TestFragmentFactory(userOrdersViewModelTest = userOrdersViewModel)
    }

    @Test
    fun launchOrdersFragment_noOrders_placeHolderTextIsVisible() {
        val orders = userOrdersViewModel.orders.getOrAwaitValue()
        assertThat(orders).isEmpty()

        launchFragmentInHiltContainer<OrdersFragment>(navHostController = navHostController, fragmentFactory = factory) {
            userOrdersViewModel.assertThatOrdersPlaceholderIsInSyncWithListState()
        }
    }

    @Test
    fun launchOrdersFragment_addUserOrder_verifyOrderList_returnToProductsFragment() {
        var orders = userOrdersViewModel.orders.getOrAwaitValue()
        assertThat(orders).isEmpty()

        userOrdersViewModel.createOrder(userId = firstUser.id, orderedProductList = orderedProductList)

        orders = userOrdersViewModel.orders.getOrAwaitValue()
        assertThat(orders).isNotEmpty()
        assertThat(orders).hasSize(1)

        val order = orders.first()
        assertThat(order.userId).isEqualTo(firstUser.id)
        assertThat(order.orderedProducts).isEqualTo(orderedProductList)

        launchFragmentInHiltContainer<OrdersFragment>(navHostController = navHostController, fragmentFactory = factory) {
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.orders_fragment)

            userOrdersViewModel.assertThatOrdersPlaceholderIsInSyncWithListState()

            onView(withId(R.id.orders_recycler_view))
                .check(matches(atPosition(0, hasDescendant(withText(getString(R.string.order_item_id, order.id))))))
                .check(matches(atPosition(0, hasDescendant(withText(getString(R.string.order_item_date, order.date))))))
                .check(matches(atPosition(0, hasDescendant(withText(getString(R.string.order_item_total_price, order.formatTotalPrice()))))))

            requireActivity().onBackPressedDispatcher.onBackPressed()

            navHostController.assertThatCurrentDestinationIsNotEqualTo(R.id.orders_fragment)
            navHostController.assertThatCurrentDestinationIsEqualTo(R.id.products_fragment)
        }
    }
}