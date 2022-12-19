package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import com.example.appcommerceclone.R
import com.example.appcommerceclone.TestNavHostControllerRule
import com.example.appcommerceclone.data.user.FakeUserAuthenticator
import com.example.appcommerceclone.di.ConnectivityModule
import com.example.appcommerceclone.di.ProductsModule
import com.example.appcommerceclone.di.UsersModule
import com.example.appcommerceclone.getOrAwaitValue
import com.example.appcommerceclone.launchFragmentInHiltContainer
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

@UninstallModules(
    ConnectivityModule::class,
    ProductsModule::class,
    UsersModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class OrdersFragmentLocalTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testNavHostControllerRule = TestNavHostControllerRule(R.id.orders_fragment)

    private lateinit var navHostController: TestNavHostController

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
    }

    @Test
    fun launchOrdersFragment_withUser_ordersSizeShouldBe2() = runTest {
        launchFragmentInHiltContainer<OrdersFragment>(navHostController = navHostController) {
            userViewModel.login(username = FakeUserAuthenticator.USERNAME, password = FakeUserAuthenticator.PASSWORD)
            advanceUntilIdle()

            val loggedUser = userViewModel.loggedUser.getOrAwaitValue()
            assertThat(loggedUser).isNotNull()

            val orders = userOrdersViewModel.orders.getOrAwaitValue()
            assertThat(orders.size).isEqualTo(2)
        }
    }

    @Test
    fun launchOrdersFragments_withNoUser_shouldNavigateToLoginFragment() {
        launchFragmentInHiltContainer<OrdersFragment>(navHostController = navHostController)

        assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.user_login_fragment)
    }
}