package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.dispatcher.DispatcherProvider
import com.example.appcommerceclone.data.user.FakeUserProvider.Companion.firstUser
import com.example.appcommerceclone.data.user.UserAuthenticator
import com.example.appcommerceclone.di.DispatcherModule
import com.example.appcommerceclone.di.UsersModule
import com.example.appcommerceclone.model.order.Order
import com.example.appcommerceclone.ui.order.OrdersAdapter.*
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
    @Inject lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var navHostController: TestNavHostController
    private lateinit var userViewModel: UserViewModel
    private lateinit var userOrdersViewModel: UserOrdersViewModel
    private lateinit var factory: TestFragmentFactory

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
        userViewModel = UserViewModel(userAuthenticator, dispatcherProvider)
        userOrdersViewModel = UserOrdersViewModel()
        factory = TestFragmentFactory(
            userViewModelTest = userViewModel,
            userOrdersViewModelTest = userOrdersViewModel
        )
    }

    @Test
    fun launchOrdersFragment_noUser_navigateToLoginFragment() = runTest {

        val user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNull()

        launchFragmentInHiltContainer<OrdersFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.orders_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)
        }
    }

    @Test
    fun launchOrdersFragment_withUser_stayInUserOrdersFragment() = runTest {

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
    fun launchOrdersFragment_withUser_createNewOrder_verifyRecyclerViewHasCorrectData() = runTest {

        userViewModel.login(username = firstUser.username, password = firstUser.password)
        advanceUntilIdle()

        val user = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(user).isNotNull()
        assertThat(user).isEqualTo(firstUser)

        var orders = userOrdersViewModel.orders.getOrAwaitValue()
        assertThat(orders).isEmpty()

        val order1 = Order(orderedProducts = orderedProductList, total = orderedProductList.getTotalPrice())
        val order2 = order1.copy()

        userOrdersViewModel.processOrder(order1, user!!.id)
        userOrdersViewModel.processOrder(order2, user.id)

        orders = userOrdersViewModel.orders.getOrAwaitValue()
        assertThat(orders).isNotEmpty()
        assertThat(orders).hasSize(2)
        assertThat(orders).contains(order1)
        assertThat(orders).contains(order2)

        launchFragmentInHiltContainer<OrdersFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isNotEqualTo(R.id.user_login_fragment)
            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.orders_fragment)

            onView(withId(R.id.orders_recyclerview))
                .check(matches(atPosition(0, hasDescendant(withText(getString(R.string.order_item_id, order1.id))))))
                .check(matches(atPosition(0, hasDescendant(withText(getString(R.string.order_item_date, order1.date))))))
                .check(matches(atPosition(0, hasDescendant(withText(getString(R.string.order_item_total_price, order1.getFormattedPrice()))))))

            onView(withId(R.id.orders_recyclerview))
                .check(matches(atPosition(1, hasDescendant(withText(getString(R.string.order_item_id, order2.id))))))
                .check(matches(atPosition(1, hasDescendant(withText(getString(R.string.order_item_date, order2.date))))))
                .check(matches(atPosition(1, hasDescendant(withText(getString(R.string.order_item_total_price, order2.getFormattedPrice()))))))
        }
    }
}