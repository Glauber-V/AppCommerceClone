package com.example.appcommerceclone.ui

import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.appcommerceclone.R
import com.example.appcommerceclone.TestNavHostControllerRule
import com.example.appcommerceclone.adapters.ProductsAdapter.ProductViewHolder
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
    UsersModule::class,
    ProductsModule::class)
@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class ProductsFragmentLocalTest {

    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testNavHostControllerRule = TestNavHostControllerRule(R.id.products_fragment)

    private lateinit var navHostController: TestNavHostController

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        navHostController = testNavHostControllerRule.findTestNavHostController()
    }

    @Test
    fun launchProductsFragment_shimmerVisibilityGone_recyclerviewVisibilityVisible() = runTest {
        launchFragmentInHiltContainer<ProductsFragment>(navHostController = navHostController) {
            advanceUntilIdle()

            val isConnected = connectionViewModel.isConnected.getOrAwaitValue()
            assertThat(isConnected).isTrue()

            val products = productViewModel.products.getOrAwaitValue()
            assertThat(products).isNotEmpty()
        }

        assertThat(navHostController.currentDestination?.id)
            .isEqualTo(R.id.products_fragment)

        onView(withId(R.id.products_shimmer))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.products_recycler_view))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun clickOnProductFromTheList_navigateToProductLessDetailFragment() = runTest {
        launchFragmentInHiltContainer<ProductsFragment>(navHostController = navHostController) {
            advanceUntilIdle()

            val isConnected = connectionViewModel.isConnected.getOrAwaitValue()
            assertThat(isConnected).isTrue()

            val products = productViewModel.products.getOrAwaitValue()
            assertThat(products).isNotEmpty()
        }

        onView(withId(R.id.products_recycler_view))
            .perform(scrollToPosition<ProductViewHolder>(1))

        onView(withId(R.id.products_recycler_view))
            .perform(actionOnItemAtPosition<ProductViewHolder>(1, click()))

        assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.product_less_detail_fragment)
    }

    @Test
    fun clickOnProductFromTheList_navigateToProductFullDetailFragment() = runTest {
        launchFragmentInHiltContainer<ProductsFragment>(navHostController = navHostController) {
            advanceUntilIdle()

            val isConnected = connectionViewModel.isConnected.getOrAwaitValue()
            assertThat(isConnected).isTrue()

            val products = productViewModel.products.getOrAwaitValue()
            assertThat(products).isNotEmpty()
        }

        onView(withId(R.id.products_recycler_view))
            .perform(scrollToPosition<ProductViewHolder>(3))

        onView(withId(R.id.products_recycler_view))
            .perform(actionOnItemAtPosition<ProductViewHolder>(3, click()))

        assertThat(navHostController.currentDestination?.id).isEqualTo(R.id.product_full_detail_fragment)
    }
}