package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.user.FakeUserAuthenticator.Companion.firstUser
import com.example.appcommerceclone.ui.order.OrdersFragment
import com.example.appcommerceclone.util.*
import com.example.appcommerceclone.viewmodels.UserOrdersViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class OrdersFragmentLocalTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testNavHostControllerRule = TestNavHostControllerRule(R.id.orders_fragment)

    @get:Rule(order = 2)
    var testFragmentFactoryRule = TestFragmentFactoryRule()

    private lateinit var navHostController: TestNavHostController
    private lateinit var userViewModel: UserViewModel
    private lateinit var userOrdersViewModel: UserOrdersViewModel
    private lateinit var factory: TestFragmentFactory

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
        userViewModel = testFragmentFactoryRule.userViewModel!!
        userOrdersViewModel = testFragmentFactoryRule.userOrdersViewModel!!
        factory = testFragmentFactoryRule.factory!!
    }

    @Test
    fun launchOrdersFragment_withUser_ordersSizeShouldBe2() = runTest {

        launch { userViewModel.login(username = firstUser.username, password = firstUser.password) }
        advanceUntilIdle()

        val loggedUser = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(loggedUser).isNotNull()
        assertThat(loggedUser).isEqualTo(firstUser)

        launchFragmentInHiltContainer<OrdersFragment>(navHostController = navHostController, fragmentFactory = factory) {

            val orders = userOrdersViewModel.orders.getOrAwaitValue()
            assertThat(orders.size).isEqualTo(2)
        }
    }

    @Test
    fun launchOrdersFragments_withNoUser_shouldNavigateToLoginFragment() = runTest {

        launch { userViewModel.login("", "") }
        advanceUntilIdle()

        val loggedUser = userViewModel.loggedUser.getOrAwaitValue()
        assertThat(loggedUser).isNull()

        launchFragmentInHiltContainer<OrdersFragment>(navHostController = navHostController, fragmentFactory = factory) {

            assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)
        }
    }
}